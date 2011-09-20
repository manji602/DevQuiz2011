
public class block {
	//member variables
			int width;
			int height;
			int blank_position;
			int[] data;
			int[] initial_data;
			public void init(int arg_w,int arg_h,int[] arg_data){
				width=arg_w;
				height=arg_h;
				data=new int[width*height];
				initial_data=new int[width*height];
				for(int i=0;i<data.length;i++){
					data[i]=arg_data[i];
					if(data[i]==0) blank_position=i;
					initial_data[i]=data[i];
				}
			}
			public void init(int arg_w,int arg_h,String arg_data){
				width=arg_w;
				height=arg_h;
				data=new int[width*height];
				initial_data=new int[width*height];
				importfromString(arg_data);
				for(int i=0;i<data.length;i++){
					if(data[i]==0) blank_position=i;
					initial_data[i]=data[i];
				}
			}
			public void init_from_hash(int arg_w,int arg_h,String arg_data_hash){
				width=arg_w;
				height=arg_h;
				data=new int[width*height];
				initial_data=new int[width*height];
				String[] data_hash_array=arg_data_hash.split(",");
				for(int i=0;i<data.length;i++){
					data[i]=Integer.parseInt(data_hash_array[i]);
					if(data[i]==0) blank_position=i;
					initial_data[i]=data[i];
				}
			}
			public boolean move(int position,int type){
				int tmp,move_target=0;
				boolean flag=false;
				if(type==0){//move_up
					move_target=position-width;
					flag=!(move_target<0 || data[move_target]==-1);
				}
				if(type==1){//move_down
					move_target=position+width;
					flag=!(move_target>data.length-1 || data[move_target]==-1);
				}
				if(type==2){//move_left
					move_target=position-1;
					flag=!(position%width==0 || data[move_target]==-1);
					//flag=true;
				}
				if(type==3){//move_right
					move_target=position+1;
					flag=!(move_target%width==0 || data[move_target]==-1);
				}
				if(flag){
					tmp=data[move_target];
					data[move_target]=data[position];data[position]=tmp;
					blank_position=move_target;
				}
				return flag;
			}
			public void print(){
				for(int i=0;i<data.length;i++){
					System.out.println(i+":"+data[i]);
				}
			}
			public void copy(block src){
				for(int i=0;i<data.length;i++){
					data[i]=src.data[i];
					if(data[i]==0) blank_position=i;
				}
			}
			public boolean equal(int[] src){
				boolean ret=false;
				int counter=0;
				for(int i=0;i<data.length;i++){
					if(src[i]==data[i]) counter++;
				}
				if(counter==data.length) ret=true;
				return ret;
			}
			public void initialize(){

				for(int i=0;i<data.length;i++){
					data[i]=initial_data[i];
					if(data[i]==0) blank_position=i;
				}
			}
			public void importfromString(String arg){
				for(int i=0;i<data.length;i++){
					data[i]=str2int(String.valueOf(arg.charAt(i)));
				}
			}
			public static int str2int(String val){
				int ret=0;
				String[] str_from={"0","1","2","3","4","5","6","7","8","9",
						"A","B","C","D","E","F","G","H","I","J",
						"K","L","M","N","O","P","Q","R","S","T",
						"U","V","W","X","Y","Z","="};
				int[] int_to=new int[37];
				for(int i=0;i<36;i++) int_to[i]=i;
				int_to[36]=-1;
				for(int i=0;i<int_to.length;i++){
					if(val.compareTo(str_from[i])==0) ret=int_to[i];
				}
				return ret;
			}
			public int get_distance(int[] arg_data_to,int width){
				int distance=0,tmp_index=0;
				//Manhattan Distance
				for(int i=0;i<data.length;i++){
					for(int j=0;j<data.length;j++){ if(data[i]==arg_data_to[j]){tmp_index=j;} }
					if(data[i]==-1){tmp_index=i;}
					distance+=Math.abs(i%width-tmp_index%width)+Math.abs(i/width-tmp_index/width);
				}
				//Invert Distance
				/*
				for(int i=0;i<data.length-1;i++){
					for(int j=i+1;j<data.length;j++){
						if(data[i]>data[j]) counter++;
					}}
				distance=counter%width+counter/width;
				*/
				return distance;
			}
			public int get_direction(int[] arg_data_goal,int priority){
				int direction=0;
				int num_of_direction=4;
				block[] move_to=new block[num_of_direction];
				int[] h_star_direction=new int[num_of_direction];
				int[] sorted_direction=new int[num_of_direction];
				for(int i=0;i<num_of_direction;i++){
					sorted_direction[i]=i;
					move_to[i]=new block();
					move_to[i].init(width,height,data);
					move_to[i].move(move_to[i].blank_position, i);
					h_star_direction[i]=move_to[i].get_distance(arg_data_goal,move_to[i].width);
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
			public int[] get_direction(int[] arg_data_goal){
				int num_of_direction=4;
				block[] move_to=new block[num_of_direction];
				int[] h_star_direction=new int[num_of_direction];
				int[] sorted_direction=new int[num_of_direction];
				for(int i=0;i<num_of_direction;i++){
					sorted_direction[i]=i;
					move_to[i]=new block();
					move_to[i].init(width,height,data);
					move_to[i].move(move_to[i].blank_position, i);
					h_star_direction[i]=move_to[i].get_distance(arg_data_goal,move_to[i].width);
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
				return sorted_direction;
			}
			public String makehash(){
				String ret="";
				for(int i=0;i<data.length;i++){
					ret=ret+data[i]+",";
				}
				return ret;
			}
			public void input_from_map(String arg_map){
				String[] data_hash_array=arg_map.split(",");
				for(int i=0;i<data.length;i++){
					data[i]=Integer.parseInt(data_hash_array[i]);
					if(data[i]==0) blank_position=i;
					//initial_data[i]=data[i];
				}
			}
}
