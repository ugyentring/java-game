package my.games.earthguardian.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import my.games.earthguardian.managers.GameManager;

public class GameObject {
    protected Vector2 position;
    protected Vector2 velocity;
    protected Texture texture;
    protected Rectangle bounds;
    protected Rectangle viewportBounds;
    protected GameManager gameManager;

    public boolean isRemove(){
        return remove;
    }

    public void setRemove(boolean remove){
        this.remove = remove;
    }

    protected boolean remove;

    public GameObject(Vector2 position, Vector2 velocity, Texture texture, Rectangle viewportBounds){
        this.position = position;
        this.velocity = velocity;
        this.texture = texture;
        this.bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        this.viewportBounds = viewportBounds;
        this.remove = false;
    }

    public void update(float deltaTime){
        position.add(velocity);
        bounds.setPosition(position.x, position.y);
    }
    public void render(SpriteBatch batch){
        batch.draw(texture, position.x, position.y, this.bounds.width, this.bounds.height);
    }
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void dispose(){
        texture.dispose();
    }

    public boolean checkCollision(GameObject other){
        return bounds.overlaps(other.bounds);
    }

    //Getters and Setters
    public Vector2 getPosition(){
        return position;
    }

    public void setPosition(Vector2 position){
        this.position = position;
        this.bounds.setPosition(position.x, position.y);
    }

    public Vector2 getVelocity(){
        return velocity;
    }

    public void setVelocity(Vector2 velocity){
        this.velocity = velocity;
    }

    public Texture getTexture(){
        return texture;
    }
    public void setTexture(Texture texture){
        this.texture = texture;
        this.bounds.setSize(texture.getWidth(), texture.getHeight());
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void setBounds(Rectangle bounds){
        this.bounds = bounds;
    }

    public Rectangle getViewportBounds(){
        return viewportBounds;
    }

    public void setViewportBounds(Rectangle viewportBounds){
        this.viewportBounds = viewportBounds;
    }

}
