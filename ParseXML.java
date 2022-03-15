import javax.xml.parsers.DocumentBuilderFactory;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class ParseXML {

	// building a document from the XML file
	// returns a Document object after loading the book.xml file.
	public Document getDocFromFile(String filename) throws ParserConfigurationException {
		{

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = null;

			try {
				doc = db.parse(filename);
			} catch (Exception ex) {
				System.out.println("XML parse failure");
				ex.printStackTrace();
			}
			return doc;
		} // exception handling

	}

	// reads all data from cards.xml and return array of scenes to be used in board
	private ArrayList<Scene> readSceneData(Document d){
		ArrayList<Scene> cardsReturn = new ArrayList<Scene>();
		Element root = d.getDocumentElement();
		NodeList cards = root.getElementsByTagName("card");
		String sceneName, sceneBudget, sceneNumber = "", sceneDescription = "", roleName, roleLevel, line = "", sceneImg;
		ArrayList<Role> rolesCard = new ArrayList<Role>();

		for (int i = 0; i < cards.getLength(); i++){
			rolesCard.clear();
			Node card = cards.item(i);
			sceneName = card.getAttributes().getNamedItem("name").getNodeValue();
			sceneImg = card.getAttributes().getNamedItem("img").getNodeValue();
			sceneBudget = card.getAttributes().getNamedItem("budget").getNodeValue();
			int roleXCoord = 0;
			int roleYCoord = 0;

			NodeList children = card.getChildNodes();
			for (int j = 1; j < children.getLength(); j++){
				
				Node sub = children.item(j);
				// if the child is the scene description, after are roles
				if("scene".equals(sub.getNodeName())){
					sceneNumber = sub.getAttributes().getNamedItem("number").getNodeValue();
					sceneDescription = sub.getTextContent();
				}
				// if the child is a role
				if("part".equals(sub.getNodeName())){
					roleName = sub.getAttributes().getNamedItem("name").getNodeValue();
					roleLevel = sub.getAttributes().getNamedItem("level").getNodeValue();

					NodeList roleChildren = sub.getChildNodes();
					for (int x = 0; x < roleChildren.getLength(); x++){
						Node subChildren = roleChildren.item(x);
						if("area".equals(subChildren.getNodeName())){
							// maybe have a comma delimited string instead of a 
							roleXCoord = Integer.parseInt(subChildren.getAttributes().getNamedItem("x").getNodeValue());
							roleYCoord = Integer.parseInt(subChildren.getAttributes().getNamedItem("y").getNodeValue());
						}
						if("line".equals(subChildren.getNodeName())){
							line = subChildren.getTextContent();
						}
					}
					Role addRole = new Role(roleName, Integer.parseInt(roleLevel), true, line, roleXCoord, roleYCoord);
					rolesCard.add(addRole);
				}
			}
			Role[] rolesArray = rolesCard.toArray(new Role[0]);
			Scene addScene = new Scene(sceneName, sceneDescription, Integer.parseInt(sceneBudget), rolesArray, Integer.parseInt(sceneNumber), sceneImg);
			cardsReturn.add(addScene);
		}
		return cardsReturn;
	} // readSceneData


	// reads all data from board.xml and returns list of rooms to be used in board
	private ArrayList<Room> ReadBoardDataSet(Document d){
		ArrayList<Room> board = new ArrayList<Room>();
		Element root = d.getDocumentElement();
		NodeList boardRoot = root.getElementsByTagName("set");
		String setName = "", roleName = "", roleLevel = "", line = "";
		ArrayList<String> neighbors = new ArrayList<String>(); // eventually to list
		int shotCounter = 0;
		int[] shotCounterArray = new int[12]; // a max of 3 coordinates
		ArrayList<Role> roles = new ArrayList<Role>();
		int iterator = 0;
		int yCoord = 0;
		int xCoord = 0;
		int roleXCoord = 0;
		int roleYCoord = 0;
		int shotCounter1x = 0;
		int shotCounter1y = 0;
		int shotCounter2x = 0;
		int shotCounter2y = 0;
		int shotCounter3x = 0;
		int shotCounter3y = 0;
		

		// get all the sets
		for(int i = 0; i < boardRoot.getLength(); i++){
			iterator = 0;
			neighbors.clear();
			roles.clear();
			shotCounter = 0;
			
			Node room = boardRoot.item(i);
			// name for the set
			setName = room.getAttributes().getNamedItem("name").getNodeValue();
			// the sets children in html
			NodeList children = room.getChildNodes();
			// iterate through the sets children

			for(int j = 0; j < children.getLength(); j++){
				Node sub = children.item(j);
				// get the neighbors
				if("neighbors".equals(sub.getNodeName())){
					NodeList neighborChildren = sub.getChildNodes();
					for(int x = 0; x < neighborChildren.getLength(); x++){
						Node neighborSub = neighborChildren.item(x);
						if("neighbor".equals(neighborSub.getNodeName())){
							neighbors.add(neighborSub.getAttributes().getNamedItem("name").getNodeValue());
						}	
					}
				}

				if ("area".equals(sub.getNodeName())){
					xCoord = Integer.parseInt(sub.getAttributes().getNamedItem("x").getNodeValue());
					yCoord = Integer.parseInt(sub.getAttributes().getNamedItem("y").getNodeValue());
				}

				if("takes".equals(sub.getNodeName())){
					NodeList takesChildren = sub.getChildNodes();
					for(int x = 0; x < takesChildren.getLength(); x++){
						Node takesSub = takesChildren.item(x);
						if("take".equals(takesSub.getNodeName())){
							// this is a sub node
							shotCounter++;
							NodeList takeChildren = takesSub.getChildNodes();
							for(int p = 0; p < takeChildren.getLength(); p++){
								Node takeSub = takeChildren.item(p);
								if ("area".equals(takeSub.getNodeName())){
										if (iterator == 0){
											shotCounter1x = Integer.parseInt(takeSub.getAttributes().getNamedItem("x").getNodeValue());
											shotCounter1y = Integer.parseInt(takeSub.getAttributes().getNamedItem("y").getNodeValue());
										}else if (iterator == 1){
											shotCounter2x = Integer.parseInt(takeSub.getAttributes().getNamedItem("x").getNodeValue());
											shotCounter2y = Integer.parseInt(takeSub.getAttributes().getNamedItem("y").getNodeValue());
										}else if (iterator == 2){
											shotCounter3x = Integer.parseInt(takeSub.getAttributes().getNamedItem("x").getNodeValue());
											shotCounter3y = Integer.parseInt(takeSub.getAttributes().getNamedItem("y").getNodeValue());
										}
										shotCounterArray[(iterator*3)] = Integer.parseInt(takeSub.getAttributes().getNamedItem("x").getNodeValue());
										shotCounterArray[(iterator*3)+1] = Integer.parseInt(takeSub.getAttributes().getNamedItem("y").getNodeValue());
										shotCounterArray[(iterator*3)+2] = Integer.parseInt(takeSub.getAttributes().getNamedItem("h").getNodeValue());
										shotCounterArray[(iterator*3)+3] = Integer.parseInt(takeSub.getAttributes().getNamedItem("w").getNodeValue());
										iterator++;
								}
							}
						}
					}
				}

				// roles
				if("parts".equals(sub.getNodeName())){
					NodeList partsChildren = sub.getChildNodes();
					for(int x = 0; x < partsChildren.getLength(); x++){
						Node partsSub = partsChildren.item(x);
						if("part".equals(partsSub.getNodeName())){
							roleName = partsSub.getAttributes().getNamedItem("name").getNodeValue();
							roleLevel = partsSub.getAttributes().getNamedItem("level").getNodeValue();
							NodeList roleChildren = partsSub.getChildNodes();
							for (int y = 0; y < roleChildren.getLength(); y++){
								Node subSub = roleChildren.item(y);
								if("area".equals(subSub.getNodeName())){
									// maybe have a comma delimited string instead of a 
									roleXCoord = Integer.parseInt(subSub.getAttributes().getNamedItem("x").getNodeValue());
									roleYCoord = Integer.parseInt(subSub.getAttributes().getNamedItem("y").getNodeValue());
								}
								if("line".equals(subSub.getNodeName())){
									line = subSub.getTextContent();
								}
							}
							Role addRole = new Role(roleName, Integer.parseInt(roleLevel), true, line, roleXCoord, roleYCoord);
							roles.add(addRole);
						}
					}
				}
			}
			Role[] roleArray = roles.toArray(new Role[0]);
			String[] neighborsArray = new String[neighbors.size()];
			neighborsArray = neighbors.toArray(neighborsArray);
			Set setAdd = new Set(shotCounter, roleArray, false, neighborsArray, setName, xCoord, yCoord, shotCounter1x, shotCounter1y,
			shotCounter2x, shotCounter2y, shotCounter3x, shotCounter3y);
			setAdd.getNeighbors();
			board.add(setAdd);
		}
		return board;
	} // ReadBoardData

	private Trailer ReadBoardDataTrailer(Document d){
		Trailer trailer = new Trailer();
		Element root = d.getDocumentElement();
		NodeList boardRoot = root.getElementsByTagName("set");
		ArrayList<String> neighbors = new ArrayList<String>(); // eventually to list
		

		boardRoot = root.getElementsByTagName("trailer");

		for(int i = 0; i < boardRoot.getLength(); i++){			
			Node room = boardRoot.item(i);
			// the sets children in html
			NodeList children = room.getChildNodes();
			// iterate through the sets children

			for(int j = 0; j < children.getLength(); j++){
				Node sub = children.item(j);
				// get the neighbors
				if("neighbors".equals(sub.getNodeName())){
					NodeList neighborChildren = sub.getChildNodes();
					for(int x = 0; x < neighborChildren.getLength(); x++){
						Node neighborSub = neighborChildren.item(x);
						if("neighbor".equals(neighborSub.getNodeName())){
							neighbors.add(neighborSub.getAttributes().getNamedItem("name").getNodeValue());
						}	
					}
				}
			}
			String[] neighborsArray = new String[neighbors.size()];
			neighborsArray = neighbors.toArray(neighborsArray);
			trailer = new Trailer("trailer", neighborsArray, "trailer");
		}

		
		return trailer;
	} // ReadBoardData

	private CastingOffice ReadBoardDataOffice(Document d){
		CastingOffice office = new CastingOffice();
		Element root = d.getDocumentElement();
		NodeList boardRoot = root.getElementsByTagName("set");
		ArrayList<String> neighbors = new ArrayList<String>(); // eventually to list

		boardRoot = root.getElementsByTagName("office");

		for(int i = 0; i < boardRoot.getLength(); i++){
			neighbors.clear();
			
			Node room = boardRoot.item(i);
			// the sets children in html
			NodeList children = room.getChildNodes();
			// iterate through the sets children

			for(int j = 0; j < children.getLength(); j++){
				Node sub = children.item(j);
				// get the neighbors
				if("neighbors".equals(sub.getNodeName())){
					NodeList neighborChildren = sub.getChildNodes();
					for(int x = 0; x < neighborChildren.getLength(); x++){
						Node neighborSub = neighborChildren.item(x);
						if("neighbor".equals(neighborSub.getNodeName())){
							neighbors.add(neighborSub.getAttributes().getNamedItem("name").getNodeValue());
						}	
					}
				}
			}
			String[] neighborsArray = new String[neighbors.size()];
			neighborsArray = neighbors.toArray(neighborsArray);
			office = new CastingOffice("office", neighborsArray, "office");
		}
		return office;

	} // ReadBoardData

	public Board readBoardAndCards(Document boardXML, Document cardsXML){
		ArrayList<Room> sets = new ArrayList<Room>();
        ArrayList<Scene> cards = new ArrayList<Scene>();
		CastingOffice office = new CastingOffice();
		Trailer trailer = new Trailer();
		trailer = ReadBoardDataTrailer(boardXML);
		office = ReadBoardDataOffice(boardXML); 
		cards = readSceneData(cardsXML);
		sets = ReadBoardDataSet(boardXML);
		Set[] roomArray = sets.toArray(new Set[0]);
        Scene[] cardArray = cards.toArray(new Scene[0]);
		Board boardClass = Board.getInstance(cardArray, roomArray, office, trailer);
		return boardClass;	
	}
}// class