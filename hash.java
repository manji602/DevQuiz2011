
public class hash {
	int max_search_way;
	int index_current;		//現在展開中のindex
	int index_end;			//データが埋め込まれている最後尾のindex
	int num_of_direction=4;
	int num_of_buffer;	//刈り取り幅
	int blank=999999;		//blank code
	int max_layer_search;
	int block_width,block_height;
	int counter=0;
	block initial_block;
	block block_from;
	block[] block_to;
	// num of buffer
	int[] min_a_star_distance;		//0,1:current 2,3:previous
	int[] min_a_star_index;
	int[] min_h_distance;
	String[] min_ismoved_map;
	String[] min_hash;
	public void init(int arg_max_way,block arg_origin,int[] answer_data,int buffer_size){
		block_width=arg_origin.width;
		block_height=arg_origin.height;
		index_current=0;index_end=0;
		//num of buffer
		num_of_buffer=block_width*block_height*buffer_size*7;
		min_a_star_distance=new int[num_of_buffer];
		min_a_star_index=new int[num_of_buffer];
		min_h_distance=new int[num_of_buffer];
		min_ismoved_map=new String[num_of_buffer];
		min_hash=new String[num_of_buffer];
		max_search_way=arg_max_way;
		block_from=new block();
		block_to=new block[num_of_direction];
		for(int i=0;i<num_of_buffer;i++){
			min_a_star_distance[i]=blank;
			min_a_star_index[i]=blank;
			min_h_distance[i]=blank;
			min_ismoved_map[i]="";min_hash[i]="";
		}
		for(int i=0;i<num_of_direction;i++) block_to[i]=new block();
		//initialize
		initial_block=new block();
		initial_block.init(arg_origin.width, arg_origin.height, arg_origin.data);
		min_ismoved_map[index_current]=initial_block.makehash();
		min_h_distance[index_current]=initial_block.get_distance(answer_data, block_width);
		min_a_star_distance[index_current]=min_hash[index_current].length()+min_h_distance[index_current];
		min_a_star_index[index_current]=index_current;
	}
	public void extract(int arg_index_target,int[] arg_goal_data,int arg_counter){
		//reset_min_data(arg_index_target);
		for(int i=0;i<num_of_buffer;i++){
			if(i==0 || max_layer_search<min_a_star_distance[i]) max_layer_search=min_a_star_distance[i];
		}
		int[] direction=new int[num_of_direction];
		int[] h_distance=new int[num_of_direction];
		int[] a_star_distance=new int[num_of_direction];
		boolean[] isenabled=new boolean[num_of_direction];
		String[] hash=new String[num_of_direction];
		String[] ismoved_map=new String[num_of_direction];		
		block_from.init_from_hash(block_width, block_height, min_ismoved_map[arg_index_target]);
		direction=block_from.get_direction(arg_goal_data);
		for(int i=0;i<num_of_direction;i++)block_to[i].init_from_hash(block_width, block_height, min_ismoved_map[arg_index_target]);
		String hash_from=min_hash[arg_index_target];
		int prohibit_direction=-1;
		if(arg_counter!=0) prohibit_direction=get_opposite_position(Integer.parseInt(""+hash_from.charAt(hash_from.length()-1)));
		reset_min_data(arg_index_target);
		for(int i=0;i<num_of_direction;i++){
			//int index_tmp=arg_index_end+(i+1);
			isenabled[i]=block_to[i].move(block_to[i].blank_position,direction[i]);
			ismoved_map[i]=block_to[i].makehash();
			if(isenabled[i]) hash[i]=hash_from+direction[i];
			else hash[i]=hash_from;
			if(arg_counter!=0 && direction[i]==prohibit_direction) isenabled[i]=false;
			h_distance[i]=block_to[i].get_distance(arg_goal_data,block_to[i].width);
			int a_star_tmp=hash[i].length()+h_distance[i];
			a_star_distance[i]=a_star_tmp;
			if(isenabled[i]){
				for(int j=0;j<num_of_buffer;j++) {
					if(ismoved_map[i].equals(min_ismoved_map[j])) {
						isenabled[i]=false;
				}}}
			if(a_star_distance[i]>max_layer_search){
				isenabled[i]=false;
			} else {
				if(isenabled[i])set_min_data(a_star_distance[i],h_distance[i],i,hash[i],ismoved_map[i]);
			}
		}
	}
	public void set_min_data(int arg_a_star_distance,int arg_h_distance,int arg_a_star_index,String arg_hash,String arg_ismoved_map){
		boolean flag=false;
		int set_index=blank;
		if(true){
			for(int j=0;j<num_of_buffer;j++){
				if(min_a_star_distance[j]==blank){
					set_index=j;flag=true;break;
				}}
			for(int j=0;j<num_of_buffer;j++){
				if(arg_a_star_distance<min_a_star_distance[j] && flag==false){
					set_index=j;flag=true;break;
				}}
			if(flag==false){
				for(int j=0;j<num_of_buffer;j++){
					if(arg_a_star_distance==min_a_star_distance[j] && arg_h_distance<min_h_distance[j]){
						set_index=j;break;
					}}}
		}
		if(set_index!=blank){
			min_a_star_distance[set_index]=arg_a_star_distance;
			min_a_star_index[set_index]=arg_a_star_index;
			min_h_distance[set_index]=arg_h_distance;
			min_hash[set_index]=arg_hash;
			min_ismoved_map[set_index]=arg_ismoved_map;
		}
	}
	public int get_opposite_position(int arg_index){
		int ret=0;
		if(arg_index==0) ret=1;
		if(arg_index==1) ret=0;
		if(arg_index==2) ret=3;
		if(arg_index==3) ret=2;
		return ret;
	}
	public void reset_min_data(int arg_index){
		min_a_star_distance[arg_index]=blank;
		min_a_star_index[arg_index]=blank;
		min_h_distance[arg_index]=blank;
		min_hash[arg_index]="";
		min_ismoved_map[arg_index]="";
	}
	public String solve(int arg_max_way,block arg_origin,int[] arg_goal_data){
		String ret="";
		int buffer_size=4;
		int initial_max_layer_search=arg_origin.get_distance(arg_goal_data, arg_origin.width)+15;
		max_layer_search=initial_max_layer_search;
		try{
			while(true){
				if(counter==0) init(arg_max_way,arg_origin,arg_goal_data,buffer_size);
				extract(index_current,arg_goal_data,counter);
				index_current=find_next_target(arg_goal_data);
				counter++;
				if(get_distance(arg_goal_data,index_current)==0){
					ret=hashconvert(min_hash[index_current]);
					System.out.println("num:"+counter);
					break;
				}
				if(counter>250000){counter=0;break; }
			}		
		} catch(Exception e){ ret=""; }
		
		return ret;
	}
	public int get_distance(int[] arg_goal_data,int arg_index){
		int ret=0;
		block current_block=new block();
		current_block.init_from_hash(block_width, block_height, min_ismoved_map[arg_index]);
		for(int i=0;i<arg_goal_data.length;i++) ret+=Math.abs(arg_goal_data[i]-current_block.data[i]);
		return ret;
	}
	public int find_next_target(int[] arg_goal_data){
		int index_ret=-1;
		boolean flag=false;
		int min_a_star_distance_=0,index=0;
		//if doubled..disable
		for(int i=0;i<num_of_buffer;i++){
			index=(int)(Math.random()*num_of_buffer);
			//index=i;
			if(min_a_star_index[index]!=blank){
				if(flag==false || min_a_star_distance[index]<=min_a_star_distance_){
					flag=true;
						min_a_star_distance_=min_a_star_distance[index];
						index_ret=index;
				}}
		}
		return index_ret;
	}
	public void min_print(int arg_index){
		System.out.print(arg_index);
		System.out.print(": hash->"+min_hash[arg_index]);
		System.out.print(" a_star_distance->"+min_a_star_distance[arg_index]);
		System.out.print(" h_distance->"+min_h_distance[arg_index]);
		System.out.print(" layer->"+min_hash[arg_index].length());
		System.out.print(" map->"+min_ismoved_map[arg_index]);
		System.out.println();
	}
	public void min_print_all(){
		for(int i=0;i<num_of_buffer;i++){
			if(min_a_star_index[i]!=blank){
				min_print(i);
			}}
	}
	public String hashconvert(String arg_hash){
		String hash_to="";
		String[] convert_from={"0","1","2","3"};
		String[] convert_to={"U","D","L","R"};
		String convert_char="";
		for(int i=0;i<arg_hash.length();i++){
			for(int j=0;j<num_of_direction;j++){
				if(String.valueOf(arg_hash.charAt(i)).equals(convert_from[j])) convert_char=convert_to[j];
			}
			hash_to+=convert_char;
		}
		return hash_to;
	}
}
