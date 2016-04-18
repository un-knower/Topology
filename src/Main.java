import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args){
		//暂时先写在这里吧，目前没仔细想如何函数化代码
		try{
			BufferedReader file_mid = new BufferedReader(new FileReader(Config.MAP_MID));
			BufferedReader file_mif = new BufferedReader(new FileReader(Config.MAP_MIF));
			int line=0;
			List<Integer> highway_lines=new ArrayList<Integer>(Config.HIGHWAY_COUNT);
			HashMap<String,RoadLink> id_RoadLink=new HashMap<String,RoadLink>();
			HashMap<String,List<String>> seNodeID_IDArray=new HashMap<String,List<String>>();
			HashMap<Integer,RoadLink> line_RoadLink=new HashMap<Integer,RoadLink>();
			String s;
			while((s=file_mid.readLine())!=null){
				line++;
				String[] s_array=s.split("\",\"|\"");
				//判断这一行是高速公路，即前两个字段是00
				if(s_array[4].charAt(0)=='0'&&s_array[4].charAt(1)=='0'){
					highway_lines.add(line);
					RoadLink temp_RoadLink=new RoadLink(s_array);
					id_RoadLink.put(s_array[2],temp_RoadLink);
					//此处需要判断道路的方向，2是从SNode到ENode，3是从ENode到SNode。一般情况下就这两种选择。
					String nodeID;
					if(s_array[6].equals("2")){//顺行
						nodeID=s_array[10];
					}
					else if(s_array[6].equals("2")){//逆行
						nodeID=s_array[11];
					}
					if(seNodeID_IDArray.containsKey(nodeID)){
						List<String> temp_array=seNodeID_IDArray.get(nodeID);
						temp_array.add(s_array[2]);
						seNodeID_IDArray.put(nodeID,temp_array);
					}
					else{
						//此处认为某个Node对应的路链最多2个，即一条路最多分2条路。
						List<String> temp_array=new ArrayList<String>(2);
						temp_array.add(s_array[2]);
						seNodeID_IDArray.put(nodeID,temp_array);
					}
					line_RoadLink.put(line,temp_RoadLink);
				}
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			file_mid.close();
			file_mif.close();
		}
	}
	
}
