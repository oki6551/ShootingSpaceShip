/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

import java.awt.Graphics;
import java.awt.Color;

/**
 *
 * @author wgpak
 */
public class Shot {

    private int x_pos;
    private int y_pos;
    private boolean alive;
    private final int radius = 3;

    public Shot(int x, int y) {
        x_pos = x;
        y_pos = y;
        alive = true;
    }

    public int getY() {
        return y_pos;
    }

    public int getX() {
        return x_pos;
    }

    public void moveShot(int speed) {
        y_pos += speed;
    }

    public void drawShot(Graphics g) {
        if (!alive) {
            return;
        }
        g.setColor(Color.yellow);
        g.fillOval(x_pos, y_pos, radius, radius);
    }

    public void collided() {
        alive = false;
    }
}
