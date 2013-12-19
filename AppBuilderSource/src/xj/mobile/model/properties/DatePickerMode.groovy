
package xj.mobile.model.properties

class DatePickerMode extends Property { 

  static values = [:]

  static final DatePickerMode Time = new DatePickerMode('Time')
  static final DatePickerMode Date = new DatePickerMode('Date')
  static final DatePickerMode DateAndTime = new DatePickerMode('DateAndTime')
  static final DatePickerMode CountDownTimer = new DatePickerMode('CountDownTimer')

  String mode

  private DatePickerMode(mode) { 
    this.mode = mode
    values[mode] = this
  }

  String toIOSString() { 
    return 'UIDatePickerMode' + mode
  }

  String toShortString() { 
    mode
  }

  String toString() { 
    "DatePickerMode.${mode}"
  }

  static hasValue(name) { 
    values.hasKey(name)
  }

  static getValue(name) { 
    values[name]
  }

}
