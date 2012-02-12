package explore3d.frames;

import explore3d.shapes.GenericFileShape;

/**
 * standard inferface
 * * @author J.J. Molenaar, J. Dooper
 */

public interface MainFrameInterface
{
	public void displayDirectory(GenericFileShape shape);
	public void displayFile(GenericFileShape shape);
	public void refresh(boolean viewport);
	public void setRotate(boolean rotate);
	public void setLogVisible(boolean logvisible);
}
