import java.util.LinkedList;
import java.util.List;

import com.pengrad.telegrambot.model.Update;

public class Model implements Subject{
	
	private List<Observer> observers = new LinkedList<Observer>();
	
	private List<Student> students = new LinkedList<Student>();
	private List<Eventos> eventos = new LinkedList<Eventos>();

	private static Model uniqueInstance;
	
	private Model(){}
	
	public static Model getInstance(){
		if(uniqueInstance == null){
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}
	
	public void registerObserver(Observer observer){
		observers.add(observer);
	}
	
	public void notifyObservers(long chatId, String studentsData){
		for(Observer observer:observers){
			observer.update(chatId, studentsData);
		}
	}
	
	public void addStudent(Student student){
		this.students.add(student);
	}
	
	// Adiciona evento
	public void addEvento(Update e){
//		this.eventos.add(e);
		this.notifyObservers(e.message().chat().id(), "TO METODO AQUI NO ADDEVENTO ");
	}
	// 

	public void searchEventos(Update update){
		
		this.notifyObservers(update.message().chat().id(), "TO AQUI NO SEARCHEVENTOS");
		
//		String NomeEvento = null;
//		
//		if("todos".equals(update.message().text())) {
//			for(Eventos el: eventos){
//				NomeEvento = el.getnomeEvento().toString();
//			}
//		}else {
//			for(Eventos evento: eventos){
//				if(evento.getnomeEvento().equals(update.message().text())){
//					NomeEvento = evento.getnomeEvento().toString();
//				}
//			}
//		}
//		
//		if(NomeEvento != null){
//			this.notifyObservers(update.message().chat().id(), NomeEvento);
//		} else {
//			this.notifyObservers(update.message().chat().id(), "Nao ha eventos");
//		}
		
	}

	public void searchStudent(Update update){
		String studentsData = null;
		for(Student student: students){
			if(student.getName().equals(update.message().text())){
				studentsData = student.getAcademicNumber();
			}
		}
		
		if(studentsData != null){
			this.notifyObservers(update.message().chat().id(), studentsData);
		} else {
			this.notifyObservers(update.message().chat().id(), "Student not found");
		}
		
	}

}
