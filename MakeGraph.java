/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cfg;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author sikde
 */
public class MakeGraph {
    
    ArrayList<String>Lines;
    boolean vis[];
    int[][] adj = new int[50][50];
    ExtractSyntax checker = new ExtractSyntax();
    int currentIndicator = 0;
    //Constructor to pass the lines of code
    public MakeGraph (ArrayList<String> lines){
        this.Lines = lines;
        vis = new boolean[Lines.size()];
    }
    
    public void start() throws IOException{
        currentIndicator =0;
        
        while(Lines.get(currentIndicator).contains("intmain(){") || Lines.get(currentIndicator).charAt(0)=='#' || (Lines.get(currentIndicator).charAt(0)=='/' && Lines.get(currentIndicator).charAt(0)=='/')){
            currentIndicator++;
        }
        
        Node root = new Node(currentIndicator,Lines.get(currentIndicator));
        currentIndicator++;
        
        System.out.println("\n\n\nroot node no: " + root.nodeNumber +"\n"+ "root node statement: "+root.Statement);
        
        makeRelations(root, false);
        
        dfs(root, -1);
        bfs(root);
        printGraph();
        saveGraph();

        //Draw drawGraph = new Draw();
        //drawGraph.main();
    }
    
    
    public Node makeRelations(Node branchRoot, boolean inLoop){
        Node par = branchRoot;
        ArrayList<Node> branchingsOfThisBranch = new ArrayList<>();

        while(currentIndicator <Lines.size()) {
        Node curNode = new Node(currentIndicator,Lines.get(currentIndicator));
        
        if(checker.isElse(curNode.Statement)){

            par.childs.add(curNode);
            currentIndicator++;
            branchingsOfThisBranch.add(makeRelations(curNode, false));
        }
        
        
        
        
        
        
        
        else if(checker.isElseIf(curNode.Statement)){

            par.childs.add(curNode);
            currentIndicator++;
            branchingsOfThisBranch.add(makeRelations(curNode, false));
        }
        
        
        
        
        
        
        else if(checker.isIf(curNode.Statement)){

            if(branchingsOfThisBranch.size()>0){
                for(int i=0; i<branchingsOfThisBranch.size(); i++){
                    branchingsOfThisBranch.get(i).childs.add(curNode);
                    branchingsOfThisBranch.clear();
                }
            }
            else{
                par.childs.add(curNode);
            }
            currentIndicator++;
            branchingsOfThisBranch.add(makeRelations(curNode, false));
        }
        
        
        
        
        
        
        else if(checker.isLoop(curNode.Statement)){
            //System.out.println("loop - "+ curNode.Statement);
            
            if(branchingsOfThisBranch.size()>0){
                for(int i=0; i<branchingsOfThisBranch.size(); i++){
                    branchingsOfThisBranch.get(i).childs.add(curNode);
                    branchingsOfThisBranch.clear();
                }
            }
            else{
                par.childs.add(curNode);
            }
            branchingsOfThisBranch.add(curNode);
            currentIndicator++;
            makeRelations(curNode, true);
        }
        
        
        
        
        
        
        else{
            //System.out.println("Statement - "+ curNode.Statement+branchingsOfThisBranch.size());
             if(branchingsOfThisBranch.size()>0){
                for(int i=0; i<branchingsOfThisBranch.size(); i++){
                    branchingsOfThisBranch.get(i).childs.add(curNode);
                }
                branchingsOfThisBranch.clear();
            }
            else{
                par.childs.add(curNode);
            }
            //branchingsOfThisBranch.add(curNode);
            currentIndicator++;
            if(checker.foundEnd(curNode.Statement)){
                if(inLoop==true) {
                    curNode.childs.add(branchRoot);
                }
                return curNode;
            }
            par = curNode;
        }
           
        
        
        
            
        
        }
        return null;
    }
    
    
    
    public void dfs(Node cur, int prev){
        //System.out.println(prev + " " + cur.nodeNumber+" "+cur.Statement);
        vis[cur.nodeNumber] = true;
        
        for(int i=0; i<cur.childs.size(); i++){
            int nodeNo = cur.childs.get(i).nodeNumber;
            if(vis[nodeNo]==false){
                dfs(cur.childs.get(i), cur.nodeNumber);
            }
        }
        
        for(int i=0; i<cur.childs.size(); i++){
            adj[cur.nodeNumber][cur.childs.get(i).nodeNumber] = 1;
        }
    }
    
    public void bfs(Node root) throws IOException{
        int[] level = new int[50];
        for(int i=0; i<50; i++) level[i] = 100000000;
        
        level[root.nodeNumber] = 1;
        Queue<Integer>q = new LinkedList<>(); // Queue pass Linked List
        q.add(root.nodeNumber);
        while(!q.isEmpty()){
            int cur = q.peek();
            q.poll();
            for(int i=0; i<50; i++){
                if(adj[cur][i]==1 && level[i]>level[cur]+1){
                    level[i] = level[cur]+1;
                    //System.out.println(i + " " + level[i]);
                    q.add(i);
                }
            }
        }
        try (FileWriter myWriter = new FileWriter("F:\\Downloads\\CFG-master\\LeveledNodes.txt")) {
            myWriter.write((Lines.size())+"\n");
            for(int i=0; i<Lines.size(); i++){
                myWriter.write(i + " " + level[i]+"\n");
            }
        }
        
    }
    public void printGraph (){

        int edgecount=0;
        System.out.println("\nAdjacency List:");
        for(int i=0; i<Lines.size(); i++){
            System.out.print("\t"+i+"  ->   ");
            for(int j=0; j<Lines.size(); j++){
                if(adj[i][j]==1){
                    edgecount++;
                    System.out.print(j+" ");
                }
            }

            System.out.println();
        }


        System.out.println("\nAdjacency Matrix:");
        for(int i=0; i<Lines.size(); i++){
            System.out.print("\t"+i+"\t");
            for(int j=0; j<Lines.size(); j++){
                System.out.print(adj[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("Total number of nodes :"+ Lines.size()+"\n");
        System.out.println("Total number of edges :"+edgecount);
        int cyclomatic=edgecount-Lines.size()+2;
        System.out.println("Cyclomatic Complexity : "+cyclomatic);
    }
    public void saveGraph() throws IOException{
        try (FileWriter myWriter = new FileWriter("F:\\Downloads\\CFG-master\\Edges.txt")) {
            //myWriter.write((Lines.size())+"\n");
            for(int i=0; i<Lines.size(); i++){
                for(int j=0; j<Lines.size(); j++){
                    if(adj[i][j]==1){
                        myWriter.write(i+" "+j+"\n");
                    }
                }
            }
        }
    }
    
}
