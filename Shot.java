package de.nicobach.game.Objects;

import de.nicobach.game.Constants;
import de.nicobach.game.Main;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Shot extends Object {

    private final double dx;
    private final double dy;
    private final double angle;
    private float liveTime = 0;


    public Shot(double x, double y, double dx, double dy, double angle){
        this.angle = angle;
        position = new Rectangle((float)x, (float)y, 5, 5); //TODO: Shotsize

        Main.shots.add(this);
        System.out.println("Angle:"+ Math.toDegrees(angle));
        if((dx <= 0 && dy <= 0) || (dx <= 0 && dy >= 0)){
            this.dx = -1.0f;
            this.dy = -1.0f;
        }else {
            this.dx = 1.0f;
            this.dy = 1.0f;
        }
    }

    @Override
    public void update(GameContainer gc, float timeSinceLastFrame) {
        liveTime += timeSinceLastFrame;
        if(liveTime < Constants.SHOTMAXTIME){

            float x = position.getX() + (float)(dx * Constants.SHOTSPEED * timeSinceLastFrame *  Math.cos(angle));
            float y = position.getY() + (float)(dy * Constants.SHOTSPEED * timeSinceLastFrame *  Math.sin(angle));
            position.setLocation(x, y);

            Rectangle[] walls = Main.world.getCollidableObjects();

            for (Rectangle wall : walls){
                if(wall != null && wall.intersects(position)){
                    //Main.shotsToRemove.add(this);
                }
            }

            if(position.intersects(Main.player.getPosition())){
                //Main.player.die();
            }


        }else{
            //Main.shotsToRemove.add(this);
        }
    }

    @Override
    public void render(Graphics g) {
        g.draw(position);
    }
}
