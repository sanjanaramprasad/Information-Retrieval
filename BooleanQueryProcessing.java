  import java.io.*;
	import java.io.File;
   import java.util.*;
   import java.util.Arrays;
	import java.util.logging.*;
//import org.apache.commons.lang.ArrayUtils;
   public class InformationRetrieval{
      static public LinkedList<Test> test = new LinkedList<Test>();
      static public HashMap<String, String[]> docID=new HashMap<String, String[]>();
      static public HashMap<String, String[]> TF = new HashMap<String,String[]>();
      static public List<String[]> testing=new ArrayList<String[]>();
		static File file1; 
		static FileOutputStream fos;		
		static OutputStreamWriter  stream;		//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

   
   	
      public static void main(String args[])throws IOException{
			file1 = new File("output.log");
			fos = new FileOutputStream(file1);
			stream = new OutputStreamWriter(fos);

         File file = new File("C:/Users/sanju/Downloads/term.idx");
         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int comparator= 0;
            while ((line = br.readLine()) != null) {
               LinkedList<Test> tfs = new LinkedList<Test>();
               LinkedList docIDs = new LinkedList();
               LinkedList result1 = new LinkedList();
               LinkedList terms_sorted=new LinkedList();
            
               //System.out.println(comparator);
               String split[]= line.split("\\\\");
               String term= split[0];
               //System.out.println(term);
               String array []= split[2].split("m");
               String postings [] = array[1].split(",");
              //System.out.println(postings.length);
               test.add(new Test(term, postings.length));
               for( String post: postings){
                  post=post.replace("[","");
                  post=post.replace("]","");
                  String docID_s= post.split("/")[0];
                  int term_frequency=Integer.parseInt(post.split("/")[1]);
                  int docID_i=Integer.parseInt(docID_s.trim());
               //String docIDS=String.format("%07d",docID_i);
                  docIDs.add(docID_i);
                  terms_sorted.add(term_frequency);
                  Collections.sort(terms_sorted);
                  tfs.add(new Test(docID_s,term_frequency));}
            	
               Collections.sort(tfs, Collections.reverseOrder());
               //System.out.println("JAJ"+tfs);
               Collections.sort(docIDs);
               String[] docID_array = new String[docIDs.size()];
               for (int i = 0; i < docIDs.size(); i++) {
                  String c=String.format("%07d",docIDs.get(i));
                  docID_array[i] = c; // Watch out for NullPointerExceptions!
               }
               String[] tfs_array = new String[tfs.size()];
               for (int i = 0; i < tfs.size(); i++) {
                  String c2=tfs.get(i).toString();
                  //System.out.println("c2"+c2);
                  tfs_array[i] = c2; // Watch out for NullPointerExceptions!
               }
            
               docID.put(term,docID_array);
               TF.put(term,tfs_array);
            	 
            
            }
            //System.out.println("this");
            //System.out.println(test);
            //System.out.println(docID);
            //System.out.println(TF);
            Collections.sort(test, Collections.reverseOrder());
         
            //System.out.println(test);
         
            InformationRetrieval r = new InformationRetrieval();
            r.getTopK(10);
            r.getPostings("!DOCTYPE");
         //System.out.println(resultant);
         //long startTime = System.currentTimeMillis();;
         //Object []result=
           r.termAtATimeQueryAnd("C:/Users/sanju/Downloads/inputt.txt");
          r.termAtATimeQueryOr("C:/Users/sanju/Downloads/inputt.txt");
			  r.documentAtATimeAnd("C:/Users/sanju/Downloads/inputt.txt");
			  r.documentAtATimeOR("C:/Users/sanju/Downloads/inputt.txt");
         
         //System.out.println(Arrays.toString(result));
         //long endTime = System.currentTimeMillis();
         //long duration = ((endTime - startTime)/1000);
         //System.out.println(duration+"seconds are used" );
         
         }}
    
    
    
      public void getTopK(int n) throws IOException{
		FileWriter fw = new FileWriter("out.txt");
      //System.out.println(test);
		String final_getTopK="Result: ";
         for (int i=0; i<n; i++) {
				final_getTopK+=test.get(i)+",";
				
            System.out.println(test.get(i));
         }
		fw.write("FUNCTION getTopK "+Integer.toString(n)+final_getTopK); 
		//fw.write(final_getTopK);
		fw.close();
      }
   
    
    
    
      public void getPostings(String query_term) throws IOException{
		FileWriter fw = new FileWriter("out.txt",true);
		
         String[] docIDs= docID.get(query_term);
         String[] tfs = TF.get(query_term);
         String finals_docIDs="Ordered by doc IDs: ";
         String finals_tf="Ordered by TF: ";
         for(String docID : docIDs){
            finals_docIDs += docID+",";
         }
         for(String tfso : tfs){
            finals_tf += tfso+",";
         }
         fw.write("FUNCTION: getPosting"+ query_term);
         fw.write(finals_docIDs);
			fw.write(finals_tf);
			fw.close();
      			
      }
   			  
       
    
   
   
   
    
   
      public void termAtATimeQueryAnd(String path)throws IOException{
			FileWriter fw = new FileWriter("out.txt",true);
         File file = new File(path);
         String query_terms[]=null;
         Map<String, String[]> terms=new HashMap<String, String[]>();
         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {query_terms =line.split(" ");
            }}
			String queryToWrite="";
         for(String termq: query_terms){
            String []postings=TF.get(termq);
            terms.put(termq,postings);
            System.out.println(terms);
				queryToWrite+=termq+",";
         }
         Map.Entry<String,String[]> entry=terms.entrySet().iterator().next();
         String key= entry.getKey();
         String[] result=entry.getValue();
         System.out.println("Key"+key);
         terms.remove(key);
         System.out.println(terms);
			int no_of_comparisons=0;
         while(!terms.isEmpty() && result!=null){
            System.out.println("RESULT"+Arrays.toString(result));
            Hashtable maptemp=new Hashtable();
            Map.Entry<String,String[]> entry2=terms.entrySet().iterator().next();
            String key2= entry2.getKey();
            String[] new_result=entry2.getValue();
            //System.out.println("RESULT"+Arrays.toString(new_result));
            String c[]=new String[(int)Math.max(result.length, new_result.length)];
            int f=0;
            for(int i=0;i<result.length;i++){
               for(int j=0;j<new_result.length;j++){
                  //System.out.println(result[i]);
                  //System.out.println(new_result[j]);
                  if(result[i]!=null && result[i].trim().equals(new_result[j].trim())){
							no_of_comparisons++;
                     System.out.println("yes");
                     System.out.println(c.length);
                     System.out.println(f);
                     c[f]=result[i];
                     f++;
                  }}}
				List<String> list = new ArrayList<String>(Arrays.asList(c));
    			list.removeAll(Collections.singleton(null));
    			result= list.toArray(new String[list.size()]);
            //result=c.clone();
            terms.remove(key2);}
				//List<String> list = new ArrayList<String>(Arrays.asList(result));
    			//list.removeAll(Collections.singleton(null));
				fw.write("FUNCTION: termAtATimeQueryAnd "+queryToWrite);
				fw.write(Integer.toString(result.length)+" documents are found");
				fw.write(Integer.toString(no_of_comparisons)+" comparisons are made");

				fw.close();
      								
      
      }	
   								
   								
   								
      public void termAtATimeQueryOr(String path)throws IOException{
			FileWriter fw = new FileWriter("out.txt",true);
         File file = new File(path);
         String query_terms[]=null;
         Map<String, String[]> terms=new HashMap<String, String[]>();
         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {query_terms =line.split(" ");
            }}
			String queryToWrite="";
         for(String termq: query_terms){
				queryToWrite+=termq+",";
            String []postings= TF.get(termq);
         						//System.out.println("postings"+Arrays.toString(postings));
            terms.put(termq,postings);
            System.out.println(terms);
         }
         Map.Entry<String,String[]> entry=terms.entrySet().iterator().next();
         String key= entry.getKey();
         String[] result=entry.getValue();
         //System.out.println("Key"+key);
         terms.remove(key);
         //System.out.println(terms);
         while(!terms.isEmpty() && result!=null){
            //System.out.println("RESULT"+Arrays.toString(result));
            Hashtable maptemp=new Hashtable();
            Map.Entry<String,String[]> entry2=terms.entrySet().iterator().next();
            String key2= entry2.getKey();
            String[] new_result=entry2.getValue();
           // System.out.println("NEW RESULT"+Arrays.toString(new_result));
            List<String> three = new ArrayList<String>();
            for(int x = 0; x < result.length; x++){
               three.add(result[x].trim());
            }
            for(int z  = 0; z < new_result.length; z++){
               if(!three.contains(new_result[z].trim())){
                  three.add(new_result[z].trim());
               }
            }
            String[] myArray = three.toArray(new String[0]);
            result=myArray;
            terms.remove(key2);
         }
         //System.out.println(Arrays.toString(result));
			List<String> list = new ArrayList<String>(Arrays.asList(result));
    			list.removeAll(Collections.singleton(null));
				fw.write("FUNCTION: termAtATimeQueryOR "+queryToWrite);
				fw.write("size "+Arrays.toString(result));
				fw.close();
      
      }
   
      public void documentAtATimeAnd(String path) throws IOException{
			FileWriter fw = new FileWriter("out.txt",true);
         File file = new File(path);
         String query_terms[]=null;
         String[][] arrays;
         List answer=new ArrayList();
         LinkedList<Test> conditions=new LinkedList<Test>();
         List<Integer> pointers=new ArrayList<Integer>();
         Map<Integer, String[]> postingList=new HashMap<Integer, String[]>();
      				 //List testing=new ArrayList();
         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {query_terms =line.split(" ");
            }}
         int term_no=0;
         int indexNo=1;
         for(String termq: query_terms){
         					
            String []postings= docID.get(termq);
            postingList.put(term_no,postings);
         						
            term_no++;}
         int numberOfArrays = term_no;
         arrays = new String[numberOfArrays][];
         int i=0;
			int j=1;
			String queryToWrite="";
         for(String termq: query_terms){
            System.out.println(termq);
         	queryToWrite+=termq+",";
            String []postings= docID.get(termq);
            int length=postings.length;
            arrays[i] = new String[length];
            arrays[i] = postings;
            System.out.println(Arrays.toString(arrays[i]));
            pointers.add(i);
            pointers.add(0);
            conditions.add(new Test(Integer.toString(j),postings.length));
            i++;
				j+=2;
            indexNo+=2;
         }
         Collections.sort(conditions);
         Test foo=conditions.get(0);
         System.out.println(conditions);
         int indexCondition=Integer.parseInt(foo.term);
         int lengthCondition=foo.length;
         System.out.println(indexCondition);
         System.out.println(lengthCondition);
         System.out.println(pointers);
      							//System.out.println(arrays[pointers.get(6)][pointers.get(7)]);
      							/*for(Test foo:conditions) {
      							Object fieldValue = foo.length;
      							System.out.println(fieldValue.toString());
      								}*/
         int n=0;
         do{	n++;
            long max=0; int flag=0;
            for(int h=0;h<pointers.size();h+=2){
               max=Math.max(max,Integer.parseInt(arrays[pointers.get(h)][pointers.get(h+1)]));}
            System.out.println(max);
            for(int k=0;k<pointers.size();k+=2){
               System.out.println(k);
               System.out.println(pointers);
               if(pointers.get(k+1)<((arrays[pointers.get(k)].length))){
                  while((Integer.parseInt(arrays[pointers.get(k)][pointers.get(k+1)])) < max && pointers.get(k+1)<((arrays[pointers.get(k)].length)-1)){
                     System.out.println(arrays[pointers.get(k)][pointers.get(k+1)]);
                     if(pointers.get(k+1)<(arrays[pointers.get(k)].length)-1){
                        pointers.set(k+1,pointers.get(k+1)+1);}}
               }}
         								//System.out.println(pointers);
            String temp="";
            for(int m=0;m<(pointers.size()-2);m+=2){
               if(Integer.parseInt(arrays[pointers.get(m)][pointers.get(m+1)])==(Integer.parseInt(arrays[pointers.get(m+2)][pointers.get(m+3)]))){
                  flag=0;
                  temp=arrays[pointers.get(m)][pointers.get(m+1)];}
               else{flag=1;}
            			
            }
            System.out.println(flag);
            if(flag==0){
               answer.add(temp);
               for(int g=0;g<pointers.size();g+=2){
                  pointers.set(g+1,pointers.get(g+1)+1);
               }
            }
            System.out.println("ANSWER");
            System.out.println(answer);
         																			
         																																	
         																																																						
         																			
         						
         }while(pointers.get(indexCondition)< lengthCondition-1);
			fw.write("FUNCTION: docAtATimeQueryAnd "+queryToWrite);
			Object[] arr = answer.toArray(new Object[answer.size()]);
			fw.write("size "+Arrays.toString(arr));
			fw.close();}
   								
   							
   											
   																					 // Watch out for NullPointerExceptions!
   				
   							 /*for(int a=0; a<numberOfArrays-1; a++){
   							 System.out.println(a);
   							 	for(int b=0;b<arrays[a].length;b++){System.out.println(b);System.out.println(arrays[a][b]);} }*/
   
   								
   
      public void documentAtATimeOR(String path) throws IOException{
			FileWriter fw = new FileWriter("out.txt",true);
         File file = new File(path);
         String query_terms[]=null;
         String[][] arrays;
         List answer=new ArrayList();
         LinkedList<Test1> conditions=new LinkedList<Test1>();
         List<Integer> pointers=new ArrayList<Integer>();
         Map<Integer, String[]> postingList=new HashMap<Integer, String[]>();
      				 //List testing=new ArrayList();
         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {query_terms =line.split(" ");
            }}
         int term_no=0;
         int indexNo=1;
         for(String termq: query_terms){
         					
            String []postings= docID.get(termq);
            postingList.put(term_no,postings);
         						
            term_no++;}
         int numberOfArrays = term_no;
         arrays = new String[numberOfArrays][];
         int i=0;
			int j=1;
			String queryToWrite="";
         for(String termq: query_terms){
            System.out.println(termq);
				queryToWrite+=termq+",";
            String []postings= docID.get(termq);
            int length=postings.length;
            arrays[i] = new String[length];
            arrays[i] = postings;
            System.out.println(Arrays.toString(arrays[i]));
            pointers.add(i);
            pointers.add(0);
				System.out.println("ith increased");
				System.out.println(i+1);
            conditions.add(new Test1(Integer.toString(j),postings.length));
            i++;
				j+=2;
            indexNo+=2;
         }
         Collections.sort(conditions,Collections.reverseOrder());
         Test1 foo=conditions.get(0);
         System.out.println(conditions);
         int indexCondition=Integer.parseInt(foo.term);
         int lengthCondition=foo.length;
         System.out.println(indexCondition);
         System.out.println(lengthCondition);
         System.out.println(pointers);
      							//System.out.println(arrays[pointers.get(6)][pointers.get(7)]);
      							/*for(Test foo:conditions) {
      							Object fieldValue = foo.length;
      							System.out.println(fieldValue.toString());
      								}*/
      int n=0;
         while(pointers.get(indexCondition)< lengthCondition-1){	n++;
            long max=0; int flag=0;
            for(int h=0;h<pointers.size();h+=2){
					//System.out.println(pointers);
					//System.out.println(pointers.get(h));
					//System.out.println(indexCondition);
					//System.out.println(lengthCondition);
					if(pointers.get(h+1)<arrays[pointers.get(h)].length ){
               max=Math.max(max,Integer.parseInt(arrays[pointers.get(h)][pointers.get(h+1)]));}
					else{
						max=Math.max(max,Integer.parseInt(arrays[pointers.get(h)][arrays[pointers.get(h)].length - 1]));}
					
            }
            System.out.println(max);
            for(int k=0;k<pointers.size();k+=2){
               //System.out.println(k);
               //System.out.println(pointers);
					//System.out.println(indexCondition);
					//System.out.println(lengthCondition);
					if(pointers.get(k+1)<((arrays[pointers.get(k)].length))){
               if(! answer.contains(arrays[pointers.get(k)][pointers.get(k+1)])){
                  answer.add(arrays[pointers.get(k)][pointers.get(k+1)]);}}
               if(pointers.get(k+1)<((arrays[pointers.get(k)].length))){
                  while((Integer.parseInt(arrays[pointers.get(k)][pointers.get(k+1)])) < max   &&   pointers.get(k+1)<((arrays[pointers.get(k)].length)-1)){
                     System.out.println(arrays[pointers.get(k)][pointers.get(k+1)]);
                  						//answer.add(arrays[pointers.get(k)][pointers.get(k+1)]);
                     if(pointers.get(k+1)<(arrays[pointers.get(k)].length)-1){
                        pointers.set(k+1,pointers.get(k+1)+1);}
                     if(! answer.contains(arrays[pointers.get(k)][pointers.get(k+1)])){
                        answer.add(arrays[pointers.get(k)][pointers.get(k+1)]);}}
               }}
         								//System.out.println(pointers);
            String temp="";
            for(int m=0;m<(pointers.size()-2);m+=2){
					if(pointers.get(m+1)< arrays[pointers.get(m)].length && pointers.get(m+3)<arrays[pointers.get(m+2)].length){
               if(Integer.parseInt(arrays[pointers.get(m)][pointers.get(m+1)])==(Integer.parseInt(arrays[pointers.get(m+2)][pointers.get(m+3)]))){
                  flag=0;
                  temp=arrays[pointers.get(m)][pointers.get(m+1)];}}
               else{flag=1;}
            			
            }
            System.out.println(flag);
            if(flag==0){
               if(! answer.contains(temp)){
                  answer.add(temp);}
               for(int g=0;g<pointers.size();g+=2){
					if(pointers.get(g+1)< arrays[pointers.get(g)].length-1){
                  pointers.set(g+1,pointers.get(g+1)+1);}
               }
            }
            else{
               if(! answer.contains(temp)){
					answer.add(temp);
					for(int g=0;g<pointers.size();g+=2){
					if(pointers.get(g+1)< arrays[pointers.get(g)].length-1){
                  pointers.set(g+1,pointers.get(g+1)+1);}
               }}}
         								
            System.out.println("ANSWER");
            System.out.println(answer);
         																			
         																																	
         																																																						
         																			
         						
         }
			fw.write("FUNCTION: docAtATimeQueryOr "+queryToWrite);
			Object[] arr = answer.toArray(new Object[answer.size()]);
			fw.write("size "+Arrays.toString(arr));
			fw.close();}						
   							
   }
   
					
				 
   class Test1 implements Comparable<Test1> {
      String term;
      int length;
   
      public Test1(String t, int s) {
         term = t; 
         length = s;
      }
   
      @Override
      public int compareTo(Test1 o) {
         int comparedLength = o.length;
         if (this.length > comparedLength) {
            return 1;
         } 
         else if (this.length == comparedLength) {
            return 0;
         } 
         else {
            return -1;
         }
      }
   
      public String getTerm(Test o){
         return o.term;}
   
   
      public String toString() {
         return term;
      }
   }			
		


					

   class Test implements Comparable<Test> {
      String term;
      int length;
   
      public Test(String t, int s) {
         term = t; 
         length = s;
      }
   
      @Override
      public int compareTo(Test o) {
         int comparedLength = o.length;
         if (this.length > comparedLength) {
            return 1;
         } 
         else if (this.length == comparedLength) {
            return 0;
         } 
         else {
            return -1;
         }
      }
   
      public String getTerm(Test o){
         return o.term;}
   
   
      public String toString() {
         return term;
      }
   }



/*class Postings1 implements Comparable<Postings1> {
	int docID;
 
	public Postings1(int s) { 
		docID = s;
	}
 
	public  toString() {
		return docID;
	}
}*
