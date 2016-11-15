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

class ShotType extends Shot {
    private int damage;

    public ShotType(int x, int y, int damage) {
        super(x, y);
        this.damage = damage;
    }
    public int getDamage() {
        return damage;
    }
    public int collided(int healthPoint) {
        alive = false;
        x_pos = 0;
        y_pos = 0;
        return (healthPoint - damage);
    }
}

public class Shot {

    protected int x_pos;
    protected int y_pos;
    protected boolean alive;
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

    public void drawShot(Graphics g, int currentShot) {
        if (!alive) {
            return;
        }
        switch(currentShot) {
            case 0:
                g.setColor(Color.yellow);
                g.fillOval(x_pos, y_pos, radius, radius);
                break;
            case 1:
                g.setColor(Color.red);
                g.fillOval(x_pos, y_pos, radius, radius);
                break;
            case 2:
                g.setColor(Color.blue);
                g.fillOval(x_pos, y_pos, radius, radius);
                break;
            case 3:
                g.setColor(Color.pink);
                g.fillOval(x_pos, y_pos, radius, radius);
                break;
        }
    }

    public void collided() {
        alive = false;
        x_pos = 0;
        y_pos = 0;
    }
}
