/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cfg;

import java.util.ArrayList;

/**
 *
 * @author rakib3004
 */
public class Node {
    int nodeNumber;
    String Statement;
    ArrayList<Node>parents = new ArrayList<>();
    ArrayList<Node>childs = new ArrayList<>();
    
    public Node(int nodeNo, String nodeStatement){
        this.nodeNumber = nodeNo;
        this.Statement = nodeStatement;
    }

    
    
    
    
}
