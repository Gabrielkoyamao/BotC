import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

public class View implements Observer{

	
	TelegramBot bot = TelegramBotAdapter.build("478668303:AAEtBGk5L6W6H546d7uTMJSP6h20Fy3Xh5U");

	//Object that receives messages
	GetUpdatesResponse updatesResponse;
	//Object that send responses
	SendResponse sendResponse;
	//Object that manage chat actions like "typing action"
	BaseResponse baseResponse;
			
	
	int queuesIndex=0;
	
	ControllerSearch controllerSearch; //Strategy Pattern -- connection View -> Controller
	ControllerSearchEventos controllerSearchEventos;
	ControllerRemoveEventos controllerRemoveEventos;
	ControllerAdd controllerAdd;

	boolean searchBehaviour = false;
	
	private Model model;
	
	public View(Model model){
		this.model = model; 
	}
	
	public void setControllerSearch(ControllerSearch controllerSearch){ //Strategy Pattern
		this.controllerSearch = controllerSearch;
	}
	
	public void setControllerSearchEventos(ControllerSearchEventos controllerSearchEventos){
		this.controllerSearchEventos = controllerSearchEventos;
	}
	
	public void setControllerRemoveEventos(ControllerRemoveEventos controllerRemoveEventos){
		this.controllerRemoveEventos = controllerRemoveEventos;
	}

	public void setControllerAdd(ControllerAdd controllerAdd){ //Strategy Pattern
		this.controllerAdd = controllerAdd;
	}
	
	public void receiveUsersMessages() {
		
		//infinity loop
		while (true){
		
			//taking the Queue of Messages
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(queuesIndex));
			
			//Queue of messages
			List<Update> updates = updatesResponse.updates();

			//taking each message in the Queue
			for (Update update : updates) {
				
				//updating queue's index
				queuesIndex = update.updateId()+1;
				
				if(this.searchBehaviour==true){
					this.callController(update);
					
				}
				
				else if(update.message().text().equals("add")){
					setControllerAdd(new ControllerAddEvento(model, this));
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Digita o evento"));
					this.searchBehaviour = true;
					
				}
				
				else if(update.message().text().equals("search")){
					setControllerSearchEventos(new ControllerSearchEventos(model, this));
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"digite o nome do evento ou todos"));
					this.searchBehaviour = true;
					
				}
				else if(update.message().text().equals("remove")) {
					setControllerRemoveEventos(new ControllerRemoveEventos(model, this));
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"digite o nome do evento ou todos"));
					this.searchBehaviour = true;
				}
				else {
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"digita ADD para adicionar um evento ou SEARCH"));
				}
				
			}

		}
				
	}
	
	
	public void callController(Update update){
		this.controllerSearch.search(update);
	}
	
	public void update(long chatId, String studentsData){
		sendResponse = bot.execute(new SendMessage(chatId, studentsData));
		this.searchBehaviour = false;
	}
	
	public void sendTypingMessage(Update update){
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}
	

}