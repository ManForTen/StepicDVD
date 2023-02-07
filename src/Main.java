import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Main {

    static int width = 900, height = 900;

    public static int[][] drawBresenhamLine(int xstart, int ystart, int xend, int yend) // Реализация подсчета координат линии между двумя точками в пространстве по алгоритму Брезенхэма
    /**
     * xstart, ystart - начало;
     * xend, yend - конец;
     * "g.drawLine (x, y, x, y);" используем в качестве "setPixel (x, y);"
     */
    {
        int x, y, dx, dy, incx, incy, pdx, pdy, es, el, err;

        dx = xend - xstart; //проекция на ось x
        dy = yend - ystart; //проекция на ось y

        incx = sign(dx); // Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт справа налево по иксу, то incx будет равен -1.
        incy = sign(dy);

        if (dx < 0)
            dx = -dx;
        if (dy < 0)
            dy = -dy;
        if (dx > dy) { // Определяем наклон отрезка:
            pdx = incx;
            pdy = 0;
            es = dy;
            el = dx;
        } else { // Если прямая больше высокая, чем длинная
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy; // Тогда в цикле будем двигаться по Y
        }
        x = xstart;
        y = ystart;
        err = el / 2;
        int[][] a = new int[el + 1][2]; // Массив для хранения точек
        a[0][0] = x;
        a[0][1] = y;

        for (int t = 0; t < el; t++) // Идём по всем точкам, начиная со второй и до последней
        {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx; // Сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += incy; // Сдвинуть влево-вправо, если цикл проходит по y
            } else {
                x += pdx; // Продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy; // Цикл идёт по иксу; сдвинуть вверх или вниз, если по y
            }
            a[t + 1][0] = x; // Добавляем точку в массив
            a[t + 1][1] = y;
        }
        return a;
    }

    private static int sign(int x) { // Метод для drawBresenhamLine - этот метод "рисует" все 9 видов отрезков. Наклонные (из начала в конец и из конца в начало каждый), вертикальный и горизонтальный - тоже из начала в конец и из конца в начало, и точку.
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;//возвращает 0, если аргумент (x) равен нулю; -1, если x < 0 и 1, если x > 0.
    }

    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Для закрытия окна
        frame.setTitle("DVD");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(dim.width / 2 - width / 2, dim.height / 2 - height / 2, width, height + 30);
        frame.getContentPane().setLayout(null); // Выравнивание, чтобы координаты объектов в дальнейшем считались от верхнего левого угла
        frame.getContentPane().setBackground(Color.BLACK); // Фон окна
        BufferedImage screamHead = ImageIO.read(new URL("https://www.freeiconspng.com/thumbs/dvd-logo-png/dvd-logo-png-31.png"));
        int screamHeadWidth = screamHead.getWidth();
        int screamHeadHeight = screamHead.getHeight();
        JLabel wIcon = new JLabel(new ImageIcon(screamHead)); // Создаем объект с картинкой, который будем размещать и двигать
        int startX = (int) (Math.random() * (width - screamHeadWidth)), startY = (int) (Math.random() * (height - screamHeadHeight)); // Случайные координаты
        wIcon.setBounds(startX, startY, screamHeadWidth, screamHeadHeight); // Устанавливаем начальное положение картинки
        frame.add(wIcon); // Добавляем картинку на форму
        while (true) { // Бесконечный цикл для перемещения картинки
            int newX = (int) (Math.random() * (width - screamHeadWidth)), newY = (int) (Math.random() * (height - screamHeadHeight)); // Задаем новую точку
            int[][] path = drawBresenhamLine(startX, startY, newX, newY); // По алгоритму Брезенхэма получаем путь (список координат к новой точке)
            startX = newX; // Обновляем точки
            startY = newY;
            for (int i = 5; i < path.length; i += 5) { // Цикл для прохода по массиву точек с нужным шагом
                try { // Таймер
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                wIcon.setBounds(path[i][0], path[i][1], screamHeadWidth, screamHeadHeight); // Перемещаем картинку в нужную точку
                wIcon.repaint(); // Перерисовываем картинку
            }
        }

    }
}