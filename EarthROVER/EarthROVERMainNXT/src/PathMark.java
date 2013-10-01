/**
 * Created with IntelliJ IDEA.
 * User: Tobias
 * Date: 01.10.13
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public class PathMark {
    private int degrees;
    private float headingDelta;
    private PathMarkType type;

    public PathMark(int degrees, PathMarkType type) {
        this.degrees = degrees;
        this.type = type;
    }

    public PathMark(float headingDelta, PathMarkType type) {
        this.headingDelta = headingDelta;
        this.type = type;
    }

    public float getHeadingDelta() {
        return headingDelta;
    }

    public void setHeadingDelta(float headingDelta) {
        this.headingDelta = headingDelta;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public PathMarkType getType() {
        return type;
    }

    public void setType(PathMarkType type) {
        this.type = type;
    }

    public enum PathMarkType {
        NONE, FORWARD, BACKWARD, TURN_LEFT, TURN_RIGHT
    }
}
