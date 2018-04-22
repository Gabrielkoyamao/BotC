
import java.util.Date;

public class Eventos {

	String nomeEvento;
	Date dataEvento;

	public Eventos(String nomeEvento, Date dataEvento){
		this.nomeEvento = nomeEvento;
		this.dataEvento = dataEvento;
	}

	public void setnomeEvento(String nomeEvento){
		this.nomeEvento = nomeEvento;
	}

	public void setdataEvento(Date dataEvento){
		this.dataEvento = dataEvento;
	}

	public String getnomeEvento(){
		return nomeEvento;
	}

	public Date getdataEvento(){
		return dataEvento;
	}
}
