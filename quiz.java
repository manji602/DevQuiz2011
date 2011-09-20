import java.io.*;

public class quiz {
	public static int block_width,block_height;
	public static void main(String args[]){
		//file(input,output)
		int counter=0;
		try{
			FileReader inFile=new FileReader("data.txt");
			FileReader inFile_result=new FileReader("output.txt");
			FileWriter outFile=new FileWriter("output_.txt");
			BufferedReader inBuffer = new BufferedReader(inFile);
			BufferedReader inBuffer_result= new BufferedReader(inFile_result);
			BufferedWriter outBuffer = new BufferedWriter(outFile);
			String readline,outline,readline_result;
			readline=inBuffer.readLine();
			readline=inBuffer.readLine();
			while((readline=inBuffer.readLine())!=null){
				counter++;
				String[] arg_array=readline.split(",");
				block_width=Integer.parseInt(arg_array[0]);
				block_height=Integer.parseInt(arg_array[1]);
				String input=arg_array[2];
				readline_result=inBuffer_result.readLine();
				if(readline_result.length()>=1){
					outBuffer.write(readline_result);
					//System.out.println(readline_result);					
				} else {
					//if(block_width*block_height<=25 && counter>3000){
					if(block_width*block_height>20){
						System.out.println(arg_array[2]);
						block quiz_block=new block();
						quiz_block.init(block_width,block_height,input);
						outline=puzzlesolve(quiz_block);
						outBuffer.write(outline);
					}
				}
				outBuffer.newLine();
				outBuffer.flush();
				
			}
			inBuffer.close();
			outBuffer.close();
		} catch(Exception e){
			System.out.println("file error");
			e.printStackTrace();
		}		
	}
	public static String puzzlesolve(block arg_block){
		String ret="";
		int[] answer_data=new int[arg_block.data.length];
		for(int i=0;i<answer_data.length;i++){
			if(arg_block.data[i]==-1){
				answer_data[i]=-1;
				} else {
				if(i!=answer_data.length-1){
					answer_data[i]=i+1;
				} else {
					answer_data[i]=0;
				}}}
		hash solve_hash=new hash();
		int search_max_way=1000000;
		ret=solve_hash.solve(search_max_way,arg_block,answer_data);
		System.out.println(ret);
		return ret;
	}
	//small functions
	public static int get_distance(int[] arg_data_from,int[] arg_data_to,int width){
		int distance=0,tmp_val,tmp_index=0;
		for(int i=0;i<arg_data_from.length;i++){
			for(int j=0;j<arg_data_from.length;j++){ if(arg_data_from[i]==arg_data_to[j]){tmp_index=j;} }
			if(arg_data_from[i]==-1){tmp_index=i;}
			tmp_val=Math.abs(i-tmp_index);
			distance+=(tmp_val%width)+(tmp_val/width);
		}
		return distance;
	}
	public static int get_direction(block block_start,int[] arg_data_goal,int priority){
		int direction=0;
		int num_of_direction=4;
		block[] move_to=new block[num_of_direction];
		int[] h_star_direction=new int[num_of_direction];
		int[] sorted_direction=new int[num_of_direction];
		for(int i=0;i<num_of_direction;i++){
			sorted_direction[i]=i;
			move_to[i]=new block();
			move_to[i].init(block_start.width,block_start.height,block_start.data);
			move_to[i].move(move_to[i].blank_position, i);
			h_star_direction[i]=get_distance(move_to[i].data,arg_data_goal,move_to[i].width);
		}
		//bubble sort
		int[] copy_direction=new int[num_of_direction];
		for(int i=0;i<num_of_direction;i++) copy_direction[i]=h_star_direction[i];
		for(int i=0;i<num_of_direction;i++){
			for(int j=i+1;j<num_of_direction;j++){
				if(copy_direction[i]>copy_direction[j] && j!=num_of_direction){
					int tmp=copy_direction[i];
					copy_direction[i]=copy_direction[j];copy_direction[j]=tmp;
					tmp=sorted_direction[i];
					sorted_direction[i]=sorted_direction[j];sorted_direction[j]=tmp;
				}
			}	
		}
		direction=sorted_direction[priority];
		return direction;
	}
	public static String makehash(int[] arg_data){
		String ret="";
		for(int i=0;i<arg_data.length;i++){
			ret=ret+arg_data[i]+",";
		}
		return ret;
	}
	public static boolean hashsearch(String[] src_hash_database,String src_hash){
		boolean ret=false;
		int counter=0;
		while(true){
			if(src_hash.equals(src_hash_database[counter])){ ret=true;break; }
			counter++;
			if(counter==src_hash_database.length) break;
		}
		return ret;
	}
	public int[] str2intarray(String str){
		int[] ret=new int[str.length()];
		for(int i=0;i<ret.length;i++) ret[i]=Integer.valueOf(String.valueOf(str.charAt(i)));
		return ret;
	}
}
