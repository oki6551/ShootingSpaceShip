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
import java.awt.Graphics;
import java.awt.Color;

public class Player {
    private int x_pos;
    private int y_pos;
    private int min_x;
    private int max_x;
    private int min_y;
    private int max_y;

    public Player(int x, int y, int min_x, int max_x, int min_y, int max_y) {
        x_pos = x;
        y_pos = y;
        this.min_x = min_x;
        this.max_x = max_x;
        this.min_y = min_y;
        this.max_y = max_y;
    }

    public void moveX(int speed) {
        x_pos += speed;
        if( x_pos < min_x) x_pos = min_x;
        if( x_pos > max_x) x_pos = max_x;
    }
    
    public void moveY(int speed) {
        y_pos += speed;
        if( y_pos < min_y ) y_pos = min_y;
        if( y_pos > max_y ) y_pos = max_y;
    }

    public int getX() {
        return x_pos;
    }

    public int getY() {
        return y_pos;
    }

    public ShotType generateShot(int damage) {
        ShotType shot = new ShotType(x_pos, y_pos, damage);

        return shot;
    }

    public void drawPlayer(Graphics g) {
        g.setColor(Color.red);
        int[] x_poly = {x_pos, x_pos - 10, x_pos, x_pos + 10};
        int[] y_poly = {y_pos, y_pos + 15, y_pos + 10, y_pos + 15};
        g.fillPolygon(x_poly, y_poly, 4);
    }
}
