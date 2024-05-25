import java.awt.*;
public class PowerUp {
private int x;
private int y;
private final int SIZE=20;
private final int SPEED=3;
private String type;

public PowerUp(int x , int y , String type) {
    this.x=x;
    this.y=y;
    this.type=type;
}
public void update(){
    y += SPEED;
}
public void draw(Graphics g){
    switch (type){
        case "extra_points":
            g.setColor(Color.YELLOW);
            break;
        case "double_shoot":
            g.setColor(Color.ORANGE);
            break;
        default:
            g.setColor(Color.WHITE);
            break;
    }
    g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
}
public Rectangle getBounds(){
    return new Rectangle(x - SIZE / 2 , y - SIZE / 2 , SIZE , SIZE );
}
public String getType() {
    return type;
}
}
