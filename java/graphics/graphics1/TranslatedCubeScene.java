package graphics1;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.ColorCube;

import graphics.Scene;

public class TranslatedCubeScene extends Scene
{
	private static final long serialVersionUID = 1L;

	protected BranchGroup createScene()
	{
		BranchGroup objRoot = new BranchGroup();

		Transform3D rotate_a = new Transform3D();
		Transform3D rotate_b = new Transform3D();
		Transform3D rotate_c = new Transform3D();

		rotate_a.rotX(Math.PI * 0.25d);
		rotate_b.rotY(Math.PI * 0.125d);
		rotate_c.rotZ(Math.PI * 0.5d);

		rotate_a.mul(rotate_b);
		rotate_a.mul(rotate_c);

		rotate_a.setTranslation(new Vector3f(0.5f, 1.0f, -2.0f));

		TransformGroup objRotate = new TransformGroup(rotate_a);

		objRoot.addChild(objRotate);
		objRotate.addChild(new ColorCube(0.4));

		objRoot.compile();
		return objRoot;
	}
}
