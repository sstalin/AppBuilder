package xj.mobile.builder

class ErrorMessages { 

  def errorMessages = []

  boolean isOkay() { 
    errorMessages.size() == 0
  }

  void printMessages(String prefix = '[Error]') { 
	errorMessages.each { e -> println prefix + ' ' + e}
  }

}