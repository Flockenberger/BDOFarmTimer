package at.flockenberger.bdoft.util;

import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;

public class TimeOffsetValueFactory extends IntegerSpinnerValueFactory { 

	  public TimeOffsetValueFactory(final int min, final int max) { 
	    super(min, max); 
	    setWrapAround(true);
	  } 

	  public TimeOffsetValueFactory(final int min, final int max, final int initialValue) { 
	    super(min, max, initialValue, 1); 
	    setWrapAround(true);
	  } 

	  @Override 
	  public void increment(final int steps) { 
	    final int min = getMin(); 
	    final int max = getMax(); 
	    final int currentValue = getValue(); 
	    final int newIndex = currentValue + steps * getAmountToStepBy(); 
	    setValue(newIndex <= max ? newIndex : (isWrapAround() ? (newIndex - min) % (max - min + 1) + min : max)); 
	  } 

	} 