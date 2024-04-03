public class Name {
	
	//Name instance variables 
	private String firstName;
	private String middleName;
	private String lastName;

	public Name(String fName, String lName) {
		firstName = fName;
		middleName = "";
		lastName = lName;
	}
	
	public Name(String fname,String mname,String lname) {

		firstName = fname;
		middleName = mname;
		lastName = lname;
	}
	
	public Name (String fullName) {
		int pos1 = fullName.indexOf(' ');
		firstName = fullName.substring(0, pos1);
		int pos2 = fullName.lastIndexOf(' ');
		if (pos1 == pos2)
			middleName = "";
		else 
			middleName = fullName.substring(pos1+1, pos2);
		lastName = fullName.substring(pos2 + 1);
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getMiddleName() {
		return middleName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFirstAndLastName() {
		return firstName + " " + lastName;
	}
	
	public String getLastCommaFirst() {
		String result = "";
		if(!middleName.equals("")) {
			result = lastName + ", " +firstName + " " + middleName;
		}
		else {
			result= lastName + ", " +firstName;
		}
		return result;
	}

	public String getInitials() {
		String result = firstName.charAt(0) + "";
		if (!middleName.equals("")) {
			result += middleName.charAt(0);
		}
		result += lastName.charAt(0);
		return result;
	}

	public String getFullName() {
		String result = firstName + " ";
		if (!middleName.equals("")) {
			result += middleName + " ";
		}
		result += lastName;
		return result;
	}
	
	  public int compareTo(Name other) {
		  String thisName=lastName + " " + firstName + " " + middleName;
		  String othername = other.lastName+ " " + other.firstName + other.middleName;
		 return thisName.compareTo(othername);
	  }

}
