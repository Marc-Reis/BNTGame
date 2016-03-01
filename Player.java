package de.nicobach.game.Objects;


import de.nicobach.game.Constants;
import de.nicobach.game.Main;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

public class Player extends Object{

    private Input input = Main.input;
    private boolean isAlive = true;
    private boolean inAir = true;
    private float jumpTime = 0;
    private boolean isJumping = false;

    private Circle playerVisual;

    public Player(){
        super.position = new Rectangle(100,100,40,40);
        this.playerVisual = new Circle(position.getCenterX(), position.getCenterY(), position.getWidth()/2);
    }

    @Override
    public void update(GameContainer gc, float timeSinceLastFrame) {
        float y = position.getY();
        float x = position.getX();
        double cx = x+position.getWidth()/2;
        double cy = y+position.getHeight()/2;


        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            double dx = ((double)input.getMouseX()- cx);
            double dy = ((double)input.getMouseY()- cy);
            double angle = Math.atan(dy/dx);
            //System.out.println("dx"+dx+" dy"+dy+" angle:"+Math.toDegrees( angle)+" cx"+cx+" cy"+cy);
            new Shot(cx,cy,dx,dy, angle);
        }


        if(inAir){
            y += Constants.GRAVITY * timeSinceLastFrame;
        }

        if(input.isKeyDown(Input.KEY_SPACE)) {
            isJumping = true;
            jumpTime = 0;
        }

        if(isJumping){
            if(jumpTime < Constants.MAXJUMPTIME){
                jumpTime += timeSinceLastFrame;
                y -= timeSinceLastFrame * (Constants.GRAVITY + Constants.JUMPSPEED);
            }else{
                isJumping = false;
            }
        }

        if(!isJumping){
            Rectangle[] rects = Main.world.getCollidableObjects();
            Rectangle tempPos = new Rectangle(position.getX(), y, position.getHeight(), position.getWidth());
            boolean tempAir = true;
            for (Rectangle r : rects){
                if(r != null && r.intersects(tempPos)){
                    y = r.getY()- position.getHeight();
                    tempAir = false;
                }
            }
            this.inAir = tempAir;
        }



        if(input.isKeyDown(Input.KEY_D)){
            float factor = (inAir) ? .3f : 1;
            x += Constants.SPEED * timeSinceLastFrame * factor;
        }
        if(input.isKeyDown(Input.KEY_A)){
            float factor = (inAir) ? .3f : 1;
            x -= Constants.SPEED * timeSinceLastFrame * factor;
        }


        if(x < 0){
            position.setX(0);
        }else if(x > gc.getWidth()){
            position.setX(gc.getWidth());
        }else{
            position.setX(x);
        }

        if(y > gc.getHeight()){
            die();
        }else{
            position.setY(y);
        }

        playerVisual.setLocation(position.getX(), position.getY());

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.white);
        //g.draw(position);
        g.fill(playerVisual);
    }

    public void die(){
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
