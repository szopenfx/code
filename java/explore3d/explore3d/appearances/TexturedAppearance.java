package explore3d.appearances;

import javax.media.j3d.Appearance;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Vector4f;

import com.sun.j3d.utils.image.TextureLoader;

import explore3d.Constants;

/**
 * Textured appearance for flat objects
 * (panels,floor, ceiling etc)
 * @author J.J. Molenaar, J.Dooper
 */
public class TexturedAppearance extends Appearance 
{
	
	public static final int FRONT_PLANE = 1324325;
	public static final int SIDE_PLANE = 8932744;
	public static final int FLAT_PLANE = 23478324;
	
	public static final int LEFT_PLANE = 1235985;
	public static final int RIGHT_PLANE = 90328474;
	public static final int BACK_PLANE = 3294874;

	private int _plane;
	private int _width;
	private int _height;
	private String _filename;
	
	/**
	 * Create a textured appearance for a particular plane, with a texture from a particular filename,
	 * with a certain width and a certain height
	 * @param plane One of FRONT_PLANE, SIDE_PLANE or FLAT_PLANE
	 * @param filename Filename of texture
	 * @param width Width of texture
	 * @param height Height of texture
	 */
	private TexturedAppearance(int plane, String filename, int width, int height)
	{
		_plane = plane;
		_width = width;
		_height = height;
		_filename = "explore3d/textures/" + filename;
		
		createTexture();
	}
	
	/**
	 * Create the texture itself
	 */
	private void createTexture()
	{
		// load image file
		TextureLoader tl = new TextureLoader(_filename, Constants.IMAGE_OBSERVER);

		// create texture object
		Texture2D t2d = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB, _width, _height);
		t2d.setImage(0, tl.getImage());

		// generate texture coordinates
		TexCoordGeneration tcg = null;
		switch(_plane)
		{
			case FRONT_PLANE:
				tcg = new TexCoordGeneration(
						TexCoordGeneration.OBJECT_LINEAR,
						TexCoordGeneration.TEXTURE_COORDINATE_2,
						new Vector4f(1.0f, 0.0f, 0.0f, 0.0f),
						new Vector4f(0.0f, 1.0f, 0.0f, 0.0f)
				);
				break;
			case SIDE_PLANE:
				tcg = new TexCoordGeneration(
						TexCoordGeneration.OBJECT_LINEAR,
						TexCoordGeneration.TEXTURE_COORDINATE_2,
						new Vector4f(0.0f, 1.0f, 0.0f, 0.0f),
						new Vector4f(0.0f, 0.0f, 1.0f, 0.0f)
				);
				break;
			case FLAT_PLANE:
				tcg = new TexCoordGeneration(
						TexCoordGeneration.OBJECT_LINEAR,
						TexCoordGeneration.TEXTURE_COORDINATE_2,
						new Vector4f(1.0f, 0.0f, 0.0f, 0.0f),
						new Vector4f(0.0f, 0.0f, 1.0f, 0.0f)
				);
				break;
		}
		tcg.setEnable(true);
		
		// put texture into appearance
		setTexture(t2d);
		setTexCoordGeneration(tcg);
	}

	/**
	 * Create non-selected appearance
	 * @param plane see private constructor
	 * @param filename see private constructor
	 * @param width see private constructor
	 * @param height see private constructor
	 * @return A TextureAppearnce instance
	 */
	public static TexturedAppearance createNormal(int plane, String filename, int width, int height)
	{
		return new TexturedAppearance(plane, filename, width, height);
	}
	
	/**
	 * Create selected appearance
	 * @param plane see private constructor
	 * @param filename see private constructor
	 * @param width see private constructor
	 * @param height see private constructor
	 * @return A TextureAppearnce instance
	 */
	public static TexturedAppearance createSelected(int plane, String filename, int width, int height)
	{
		TransparencyAttributes tratr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.4f);
		
		TexturedAppearance ta = new TexturedAppearance(plane, filename, width, height);
		ta.setTransparencyAttributes(tratr);
		
		return ta;
	}
}
