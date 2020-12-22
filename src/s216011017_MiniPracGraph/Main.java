/**
 * 
 */
package s216011017_MiniPracGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import Graph.Graph;
import Graph.Graph.CostPathPair;
import Graph.Algorithms.BreadthFirstTraversal;
import Graph.Algorithms.DepthFirstTraversal;

/**
 * @author tiago
 *
 */


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{

    private static final List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
    private static final List<Graph.Edge<Integer>> edges = new ArrayList<Graph.Edge<Integer>>();
    private static final Graph.Vertex<Integer> powerPlantNode = new Graph.Vertex<Integer>(0,0);
    private static Circle node[]; //showing the vertices with circles and saving them in an array
    private Graph<Integer> graph = new Graph<Integer>(Graph.TYPE.DIRECTED, vertices, edges);
    private ArrayList<Line> edge; //showing and saving the edges between vertices in an arraylist
    private ArrayList<Integer> bfs; //saving the vertices ordering by bfs algorithm
    private ArrayList<Integer> dfs; //saving the vertices ordering by dfs algorithm
    
    private int distance ;
    private int node0 ;
    private int node1 ;
    private int totaledgecount;
    private int[][] adjacencyMatrix = null;

    private CostPathPair<Integer> cost = null;
    private TextArea taInfo = new TextArea();
    
	/**
	 * @param args
	 */
	@Override
	public void start(Stage primaryStage) throws Exception{
		
		//this is a calculation to find the total edges based on vertices and laws applied to the graph
        int edgecount = ((graph.getVertices().size()*(graph.getVertices().size() - 1))/2); 
        totaledgecount = edgecount;
        
        Group root = new Group();
        VBox vbox = new VBox();
        VBox vboxFull = new VBox();
        VBox vboxtxtArea = new VBox();
        BorderPane borderpane = new BorderPane();

        Button btnAddNode = new Button("Add Supplier"); //create a button
        Button btnSetDist = new Button("Set Distances"); 
        Button btnDFS = new Button("Apply DFS"); 
        Button btnBFS = new Button("Apply BFS"); 
        Button btnClear = new Button("Clear Graph"); 

        VBox.setVgrow(vbox, Priority.NEVER);		//Sets the vertical grow priority for the child when contained by an vbox
        vbox.setPadding(new Insets(10,10,450,10));
        vboxtxtArea.setMaxHeight(200);
        vboxtxtArea.setAlignment(Pos.BOTTOM_CENTER); //Appends the specified element to the end of this list (optionaloperation).Sets the value of the property alignment
        vbox.getChildren().addAll(btnAddNode,btnSetDist,btnDFS, btnBFS,btnClear);
        vboxtxtArea.getChildren().addAll(taInfo);
        vboxFull.getChildren().addAll(vbox,vboxtxtArea);
        borderpane.setRight(vboxFull);
        borderpane.setLeft(root);
        vbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

        taInfo.setPrefHeight(300);  //sets height of the TextArea 
        taInfo.setPrefWidth(300); 
        
		graph.getVertices().add(powerPlantNode); //Appends the specified element to the end of this list (optional operation).
		txtArea();
        System.out.println(graph.toString());
        
        drawGraph(root); 
        
        btnAddNode.setOnAction(new EventHandler<ActionEvent>() {
            int count = 1;
			@Override
			public void handle(ActionEvent arg0) {
				root.getChildren().clear();
				System.out.println("Hello add sup"); 
				// Create the new dialog
	            TextInputDialog dialogSup = new TextInputDialog();
	            dialogSup.setHeaderText("How much coal does this supplier have (Megatonnes)");
	            dialogSup.setGraphic(null);
	            // Show the dialog and capture the result.
	            Optional<?> resultSup = dialogSup.showAndWait();
				System.out.println("Supplier number: " + resultSup.get().toString()); 
	            int coal = Integer.parseInt(resultSup.get().toString());
	        
	            //Lists that support this operation may place limitations on what elements may be added to this list
	            graph.getVertices().add(new Graph.Vertex<Integer>(count,coal));	
	            
	            System.out.println("Sup weight: " + coal);
	            System.out.println("Sup size: " + (count));
	            System.out.println("Graph node count: " + (graph.getVertices().size()));
	            System.out.println("Graph edge count: " + (graph.getEdges().size()));

				System.out.println(graph);
				count++;
		        drawGraph(root); 
		        txtArea();
			}        	
        });

        btnSetDist.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				root.getChildren().clear();
				System.out.println("Hello connect sup"); 
				
	            int count = 0;
	            int edgecount = ((graph.getVertices().size()*(graph.getVertices().size() - 1))/2); 
	            totaledgecount = edgecount;
	            adjacencyMatrix = new int[totaledgecount][totaledgecount];
	            System.out.println("Edge count: " + totaledgecount);
	            
	            for(int i = 0; i < totaledgecount;i++)
	            {
	            	// Create the new dialog
		            TextInputDialog dialogSupCon = new TextInputDialog();
		            dialogSupCon.setHeaderText("Enter Distance Location Location");
		            dialogSupCon.setGraphic(null);
		            // Show the dialog and capture the result.
		            Optional<?> resultSupCon = dialogSupCon.showAndWait();
		            StringTokenizer st = new StringTokenizer(resultSupCon.get().toString()," ");

	            	distance = Integer.parseInt(st.nextToken().toString());
	            	node0 = Integer.parseInt(st.nextToken().toString());
		            node1 = Integer.parseInt(st.nextToken().toString());
		            
		            setCost(new Graph.CostPathPair<Integer>(distance, graph.getAllEdges()));
		            
		            txtArea();
	            	
		            graph.getEdges().add(new Graph.Edge<Integer>(distance, graph.getVertices().get(node0), graph.getVertices().get(node1)));
            		
		         // Create the new dialog
		            TextInputDialog dialogSupRel = new TextInputDialog();
		            dialogSupRel.setHeaderText("Is this a reputable supplier?");
		            dialogSupRel.setGraphic(null);
		            // Show the dialog and capture the result.
		            Optional<?> resultSupRelCon = dialogSupRel.showAndWait();

	            	System.out.println(resultSupRelCon.get().toString().toUpperCase());
		            if(resultSupRelCon.get().toString().toUpperCase().contentEquals("YES"))
		            {
		            	System.out.println("hello stron con");
			            adjacencyMatrix[node0][node1] = 1;
		            	System.out.println("hello stron con " + node0 + " "+ node1);
		            }else if(resultSupRelCon.get().toString().toUpperCase().contentEquals("NO"))
		            {
			            adjacencyMatrix[node0][node1] = 0;
		            }

	            }

	            
	    		for(int i = 0; i < edgecount; i++)
	    		{
	    			for(int m = 0; m < edgecount; m++)
	    			{
	    				System.out.println(adjacencyMatrix[i][m]);
	    				count++;
	    			}
	    		}

				System.out.println(count);
				System.out.println(graph);
	            System.out.println("Graph edge count: " + (graph.getEdges().size()));
	            
		        drawGraph(root); 
		        txtArea();
			}
        });
        
        btnDFS.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) 
			{
		        drawDFS(borderpane,root); 
		        txtArea();
			}        	
        });
        
        btnBFS.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) 
			{
		        drawBFS(borderpane,root); 
		        txtArea();
			}        	
        });
        
        btnClear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				root.getChildren().clear();
				graph = null;
				graph = new Graph<Integer>(Graph.TYPE.DIRECTED, vertices, edges);
				graph.getVertices().add(powerPlantNode);
				drawGraph(root);
				txtArea();
			}
		});
        
        Scene scene = new Scene(borderpane);

        primaryStage.setTitle("Eskom resource trip planner");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}


    public <T> void drawGraph(Group root) 
    {

    		edge = new ArrayList<Line>();
    		node = new Circle[graph.getVertices().size()];

        for (int i = 0; i < graph.getVertices().size(); i++) 
        {
            node[i] = new Circle(17, Color.LIGHTCORAL);
            node[i].setEffect(new DropShadow(1, 3, 3, Color.GRAY));

            node[i].setCenterX((Math.random()) * 800);
            node[i].setCenterY((Math.random()) * 400);

            root.getChildren().add(node[i]);
        }

        for (int i = 0; i < graph.getVertices().size(); i++) 
        {
            for (int j = i; j < graph.getVertices().size(); j++) 
            {
            	
                edge.add(new Line(node[i].getCenterX(), node[i].getCenterY(), node[j].getCenterX(), node[j].getCenterY()));
              
                Text t = new Text(node[i].getCenterX() - 4, node[i].getCenterY() + 4, "" + (i));
                t.setFill(Color.AQUA);
                t.setStroke(Color.WHITE);
                root.getChildren().add(t);
            }
        }
        root.getChildren().addAll(edge);
        for (int i = 0; i < edge.size(); i++) 
        {
        	edge.get(i).toBack();
        }
    }

    public void drawBFS(BorderPane bp, Group root) 
    {
	
		final int[] bfsresult = BreadthFirstTraversal.breadthFirstTraversal(graph.getVertices().size(),adjacencyMatrix,powerPlantNode.getWeight());
		 
        setBfs(new ArrayList<>(graph.getVertices().size()));    
        
        Button b = new Button("START"); //create a button
        Circle c = new Circle(17, null); //create the circle will be moved on the vertices
        c.setVisible(false); //
        c.setStrokeWidth(3);
        c.setStroke(Color.BLACK);
        Text result = new Text(100, 30, "RESULT: ");
        result.setStrokeWidth(10);
        result.setFill(Color.BLACK);

        HBox hb = new HBox();
        hb.getChildren().addAll(b,result);
        root.getChildren().add(result);
        bp.setTop(hb);

        b.setOnAction(new EventHandler<ActionEvent>() {
            int count = 0;//
       	 //bfs test
            //
            @Override
            public void handle(ActionEvent event) 
            {
            
                c.setVisible(true);
                //
                b.setText("NEXT");
                // 
                if (count == graph.getVertices().size()) 
                {
                    c.setVisible(false);
                    System.out.println("wataguan");
                    System.exit(0);
                } else 
                {
                    //bfs implementation
                    c.setCenterX(node[(bfsresult[count])].getCenterX());
                    c.setCenterY(node[(bfsresult[count])].getCenterY());

                    //
                    node[(bfsresult[count])].setEffect(new Glow(.1));
                    node[(bfsresult[count])].setFill(Color.YELLOWGREEN);

                    //
                    root.getChildren().add(new Text(160 + 15 * bfsresult[count], 30, "" + (bfsresult[count])));
                    count++;

                    //
                    if (count == graph.getVertices().size()) 
                    {
                        b.setText("END");
                    }
                }
            }
        });
    }

    public void drawDFS(BorderPane bp, Group root) 
    {
    	final int[] dfsresult = DepthFirstTraversal.depthFirstTraversal(graph.getVertices().size(),adjacencyMatrix,powerPlantNode.getWeight());
 		
        setDfs(new ArrayList<>(graph.getVertices().size()));
        Button b = new Button("START"); 
        Circle c = new Circle(17, null);
        c.setVisible(false); //
        c.setStrokeWidth(3);
        c.setStroke(Color.BLACK);
        Text result = new Text(100, 30, "RESULT: ");
        result.setStrokeWidth(10);
        result.setFill(Color.BLACK);

        HBox hb = new HBox();
        hb.getChildren().addAll(b,result);
        root.getChildren().add(result);
        bp.setTop(hb);
        
        
        b.setOnAction(new EventHandler<ActionEvent>() {
        	int count = 0;
            @Override
            public void handle(ActionEvent event) {

                System.out.println("Count: " + count);
                c.setVisible(true);
                b.setText("NEXT");

                if (count == graph.getVertices().size()) 
                {
                    c.setVisible(false);
                    System.out.println();
                    System.exit(0);
                } else 
                {
                   
                	   c.setCenterX(node[dfsresult[count]].getCenterX());
					   c.setCenterY(node[dfsresult[count]].getCenterY());
					  
					   node[dfsresult[count]].setEffect(new Glow(.1));
					   node[dfsresult[count]].setFill(Color.YELLOWGREEN);
					   
					   root.getChildren().add(new Text(160 + 15 * dfsresult[count], 30, "" +dfsresult[count])); 
					   count++;
					

                    if (count == graph.getVertices().size()) 
                    {
                        b.setText("END");
                    }
                }
            }
        });
    }
    
    public void txtArea() 
    {
    	 taInfo.clear();
    	 String ppnode = "PowerPlant: ";	
    	 taInfo.appendText("Node value Coal-weight\n" + ppnode + graph.getVertices().get(0).getWeight() + " " + graph.getVertices().get(0).getWeight() + " Mt");
     	
    		 for(int i = 1;i<graph.getVertices().size() - 1; i++)
  	    	{    		
  	    		taInfo.appendText("\n" + "Supplier " + graph.getVertices().get(i).getValue() + " " + graph.getVertices().get(i).getWeight() + "Mt");
  	        	
  	        }
    		 taInfo.appendText("\n"+"Cost between nodes" + "\n");
    		 int totalCost = 0;
    		 
    			 for(int i = 0; i < graph.getAllEdges().size()-1; i++)
        		 {
        	    	 taInfo.appendText("\n" + "Cost to supplier: " + i+ " is " + graph.getEdges().get(i).getCost());
        	    	 totalCost +=  graph.getEdges().get(i).getCost();
        		 }
    		 
    		 taInfo.appendText("\n" + "Total Cost: " + totalCost);
    	 }

	public ArrayList<Integer> getBfs() 
	{
		return bfs;
	}

	public void setBfs(ArrayList<Integer> bfs) 
	{
		this.bfs = bfs;
	}

	public ArrayList<Integer> getDfs() 
	{
		return dfs;
	}

	public void setDfs(ArrayList<Integer> dfs) 
	{
		this.dfs = dfs;
	}

	public CostPathPair<Integer> getCost() 
	{
		return cost;
	}

	public void setCost(CostPathPair<Integer> cost) 
	{
		this.cost = cost;
	}
    	
}

