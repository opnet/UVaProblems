package uvaproblems.pagehopping;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Given a list of connecting nodes, find the shortest average distance between any node.
 */
class Main
{
	public static void main(String[] args) throws IOException
	{
		Main work = new Main();
		work.begin();
	}
	
	/**
	 * A list/tree hybrid. 
	 */
	static class GraphList
	{
		private HashMap<Integer, Node> nodes;
		
		GraphList()
		{
			nodes = new HashMap<>();
		}
		
		public boolean add(int n)
		{
			if(nodes.containsKey(n))
				return false;
			
			nodes.put(n, new Node(n));
			return true;
		}
		
		public void linkTo(int from, int to)
		{
			if(nodes.containsKey(from) && nodes.containsKey(to))
				nodes.get(from).addLink(nodes.get(to));
		}
		
		private int findShortestPath(Node from, Node to, HashSet<Node> alreadyChecked, int count)
		{
			if(from.nodeNumber() == to.nodeNumber())
				return count;
			
			else if(from.getNodes().isEmpty())
				return -1;
			
			for(Node n : from.getNodes())
			{
				if(n.nodeNumber == to.nodeNumber)
					return count + 1;
			}
			
			alreadyChecked.add(from);
			
			int prevMax = Integer.MAX_VALUE;
			int currMax = 0;
			
			for(Node n : from.getNodes())
			{
				if(!alreadyChecked.contains(n))
					currMax = findShortestPath(n, to, alreadyChecked, count + 1);
				
				if(currMax > 0 && currMax < prevMax)
					prevMax = currMax;
			}
			
			return prevMax;
		}
		
		public int findShortestPath(int from, int to)
		{
			if(nodes.containsKey(from) && nodes.containsKey(to))
				return findShortestPath(nodes.get(from), nodes.get(to), new HashSet<Node>(), 0);
				
			return -1;
		}
		
		public double findAverageShortestDistance()
		{
			int sum = 0;
			int total = nodes.size() * (nodes.size() - 1);
			
			for(Node from : nodes.values())
				for(Node to : nodes.values())
				{
					if(from != to)
						sum += findShortestPath(from, to, new HashSet<Node>(), 0);
				}
			
			return (double)sum / total;
		}
		
		private class Node
		{
			private final int nodeNumber;
			private HashMap<Integer, Node> linkedNodes;
			
			public Node(int nodeNumber)
			{
				this.nodeNumber = nodeNumber;
				linkedNodes = new HashMap<>();
			}
			
			public int nodeNumber()
			{
				return nodeNumber;
			}
			
			public void addLink(Node n)
			{
				linkedNodes.put(n.nodeNumber(), n);
			}
			
			public Collection<Node> getNodes()
			{
				return linkedNodes.values();
			}
			
			@Override
			public int hashCode()
			{
				final int prime = 31;
				int result = 1;
				result = prime * result + nodeNumber;
				return result;
			}
	
			@Override
			public boolean equals(Object obj)
			{
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (!(obj instanceof Node))
					return false;
				Node other = (Node) obj;
				if (nodeNumber != other.nodeNumber)
					return false;
				return true;
			}
		}
	}
	
	void begin() throws IOException
	{
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		DecimalFormat format = new DecimalFormat("##########.000");
		
		ArrayList<Point> connections = new ArrayList<>();
		int input1 = -1, input2 = -1;
		int count = 0;
		String line;
		
		while(true)
		{
			
			// LINE PARSING CODE ===============================================================
			line = stdin.readLine();
			
			if(line.charAt(0) == '0')
				break;
			
			int beg = 0;
			int end = 0;
			
			while(end < line.length())
			{
				while(end < line.length() && line.charAt(end) != ' ')
					end++;
				
				input1 = Integer.parseInt(line.substring(beg, end));
				
				// end of input, we can quit.
				if(input1 == 0)
					break;
				
				beg = ++end;
				
				while(end < line.length() && line.charAt(end) != ' ')
					end++;
				
				input2 = Integer.parseInt(line.substring(beg, end));
				beg = ++end;
				
				connections.add(new Point(input1, input2));
			}
			// LINE PARSING CODE ===============================================================
			
			// end of input, we can quit.
			if(connections.isEmpty())
				break;
			
			count++;
			GraphList list = new GraphList();
			for(Point p : connections)
			{
				list.add(p.x);
				list.add(p.y);
				list.linkTo(p.x, p.y);
			}
			
			System.out.println("Case " + count + ": average length between pages = " + format.format(list.findAverageShortestDistance()) + " clicks");
			
			connections.clear();
		}
	}
}