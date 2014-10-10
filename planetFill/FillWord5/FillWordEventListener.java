/* FillWordEventListener.java
   FillWordEventListener is a listener interface for FillWordEvent.
   Dr. Dale Parson, CSC 243, Spring 2013
*/

package FillWord5 ;

/**
    Interface FillWordEventListener is a listener interface for
    FillWordEvent that propagates changes in game state to listeners.
    @see FillWordEvent
    @author Professor Dale Parson
**/
public interface FillWordEventListener extends java.util.EventListener {
    /**
     *  processGameEvent is a client listener's method that receives a
     *  FillWordEvent when a game changes state.
     *  @see FillWordEvent
    **/
    public void processGameEvent(FillWordEvent event) ;
}
