package application;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;

public class Members {
	
	int id;
	String nom;
	String prenom;
	String gender;
	String status;
	int numPhone;
	int amount;
	int duree;
	int age;
	private SimpleObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
	private SimpleObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
	
	
	public Members(int id, String nom, String prenom, String gender, String status, int numPhone, int amount, int duree, int age, LocalDate endDate, LocalDate StartDate) {
	    super();
	    this.id = id;
	    this.nom = nom;
	    this.prenom = prenom;
	    this.gender = gender;
	    this.status = status;
	    this.numPhone = numPhone;
	    this.amount = amount;
	    this.duree = duree;
	    this.age = age;
	    this.endDate = new SimpleObjectProperty<>(endDate);
	    this.setStartDate(new SimpleObjectProperty<>(StartDate));
	}

	
	public Members(int id, String nom, String prenom, String gender, String status, int numPhone, int amount, int duree, int age, LocalDate endDate) {
	    super();
	    this.id = id;
	    this.nom = nom;
	    this.prenom = prenom;
	    this.gender = gender;
	    this.status = status;
	    this.numPhone = numPhone;
	    this.amount = amount;
	    this.duree = duree;
	    this.age = age;
	    this.endDate = new SimpleObjectProperty<>(endDate);
	}

	public Members(int id, String nom, String prenom,  int numPhone, String status) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.status = status;
		this.numPhone = numPhone;
	}
	
	
	public Members( String nom, String prenom, String gender, int numPhone, int amount, int duree,int age) {
		
		this.nom = nom;
		this.prenom = prenom;
		this.gender = gender;
		this.numPhone = numPhone;
		this.amount = amount;
		this.duree = duree;
		this.age = age;
	}


		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}


		public String getNom() {
			return nom;
		}


		public void setNom(String nom) {
			this.nom = nom;
		}


		public String getPrenom() {
			return prenom;
		}


		public void setPrenom(String prenom) {
			this.prenom = prenom;
		}


		public String getGender() {
			return gender;
		}


		public void setGender(String gender) {
			this.gender = gender;
		}


		public String getStatus() {
			return status;
		}


		public void setStatus(String status) {
			this.status = status;
		}


		public int getNumPhone() {
			return numPhone;
		}


		public void setNumPhone(int numPhone) {
			this.numPhone = numPhone;
		}


		public int getAmount() {
			return amount;
		}


		public void setAmount(int amount) {
			this.amount = amount;
		}


		public int getDuree() {
			return duree;
		}


		public void setDuree(int duree) {
			this.duree = duree;
		}


		public int getAge() {
			return age;
		}


		public void setAge(int age) {
			this.age = age;
		}
	
		@Override
		public String toString() {
		    return "Members{" +
		            "id=" + id +
		            ", nom='" + nom + '\'' +
		            ", prenom='" + prenom + '\'' +
		            ", numPhone=" + numPhone +
		            ", status='" + status + '\'' +
		            '}';
		}
		
		public LocalDate getEndDate() {
	        return endDate.get();
	    }

	    public void setEndDate(LocalDate endDate) {
	        this.endDate.set(endDate);
	    }

	    public SimpleObjectProperty<LocalDate> endDateProperty() {
	        return endDate;
	    }


		public SimpleObjectProperty<LocalDate> getStartDate() {
			return startDate;
		}


		public void setStartDate(SimpleObjectProperty<LocalDate> startDate) {
			this.startDate = startDate;
		}
	
}
