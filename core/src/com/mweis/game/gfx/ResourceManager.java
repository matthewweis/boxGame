package com.mweis.game.gfx;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;

public class ResourceManager {
	
	private static final ObjectMap<String, Sprite> sprites = new ObjectMap<String, Sprite>();
	private static final ObjectMap<String, Texture> textures = new ObjectMap<String, Texture>();
	
	public static Sprite loadSprite(String id, String file) {
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal(file)));
		putSprite(id, sprite);
		return sprite;
	}
	
	public static Sprite loadSprite(String id, String file, int srcX, int srcY, int srcWidth, int srcHeight) {
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal(file)), srcX, srcY, srcWidth, srcHeight);
		putSprite(id, sprite);
		return sprite;
	}
	
	private static void putSprite(String id, Sprite sprite) {
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		sprites.put(id, sprite);
	}
	
	public static void loadTexture(String id, String file) {
		Texture t = new Texture(Gdx.files.internal(file));
		t.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		Gdx.app.log("ResourceManager", "loaded " + id);
		if (!isPowerOfTwo(t.getWidth()) || !isPowerOfTwo(t.getHeight())) {
			Gdx.app.error("ResourceManager", "non-power-of-two texture added");
		}
		textures.put(id, t);
	}
	
	private static boolean isPowerOfTwo(int n) {
		return (n & (n - 1)) == 0;
	}
	
	// direct return can cause problems if sprites are scaled or modified
	public static Sprite getSprite(String id) {
		return sprites.get(id);
	}
	
	public static Texture getTexture(String id) {
		return textures.get(id);
	}
	
	public static void dispose() {
		// Iterator avoids ConcurrentModificationException
		Entries<String, Texture> it = textures.entries();
	    while (it.hasNext()) {
	        it.next().value.dispose();
	        it.remove();
	    }
	}
	
	private ResourceManager() {};
}
