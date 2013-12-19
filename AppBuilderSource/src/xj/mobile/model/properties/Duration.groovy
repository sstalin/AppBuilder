
package xj.mobile.model.properties

class Duration { 
  
  enum Unit { Milli_Second, Second }

  Unit unit
  float value

  Duration(float value, Unit unit = Unit.Second) { 
	this.value = value
	this.unit = unit 
  }

  String toString() { 
	"${value} ${unit}"
  }

  int getValueInMilli() { 
	switch (unit) { 
	case Unit.Milli_Second: return (int) value
	case Unit.Second: return (int) value * 1000 
	default: return (int) value * 1000 
	}
  }

  float getValueInSecond() { 
	switch (unit) { 
	case Unit.Milli_Second: return (float) (value / 1000)
	case Unit.Second: return value
	default: return value
	}
  }

}