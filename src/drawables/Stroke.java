package drawables;
import java.awt.BasicStroke;

/**
 * This class functions likes the BasicStroke class
 * but follow Beans convention and can be saved by XMLEncoder
 * 
 * @author Coolprosu
 * Jan 27, 2009 1:13:27 AM
 */

public class Stroke extends BasicStroke{
     float lineWidth, miterLimit, dashArray[], dashPhase;
     int endCap, lineJoin;
     public Stroke(int width) {
    	 super(width);
    	 this.lineWidth = width;
     }
     public Stroke(float width, int cap, int join, float miterLimit, float[] dash, float dash_phase){
          super(width,cap,join,miterLimit,dash,dash_phase);
          this.lineWidth = width;
          this.endCap = cap;
          this.lineJoin = join;
          this.miterLimit = miterLimit;
          this.dashArray = dash;
          this.dashPhase = dash_phase;
     }
     public float getLineWidth() {
          return lineWidth;
     }
     public void setLineWidth(float lineWidth) {
          this.lineWidth = lineWidth;
     }
     public float getMiterLimit() {
          return miterLimit;
     }
     public void setMiterLimit(float miterLimit) {
          this.miterLimit = miterLimit;
     }
     public float[] getDashArray() {
          return dashArray;
     }
     public void setDashArray(float[] dashArray) {
          this.dashArray = dashArray;
     }
     public float getDashPhase() {
          return dashPhase;
     }
     public void setDashPhase(float dash_phase) {
          this.dashPhase = dash_phase;
     }
     public int getEndCap() {
          return endCap;
     }
     public void setEndCap(int endCap) {
          this.endCap = endCap;
     }
     public int getLineJoin() {
          return lineJoin;
     }
     public void setLineJoin(int lineJoin) {
          this.lineJoin = lineJoin;
     }
}