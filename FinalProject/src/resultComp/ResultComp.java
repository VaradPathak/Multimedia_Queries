package resultComp;

import java.util.Comparator;

import Result.Result;

	
public class ResultComp implements Comparator<Result>{

		@Override
		public int compare(Result o1, Result o2) {
			// TODO Auto-generated method stub
			
			if(o1.rank_value > o2.rank_value) 
				return 1;
			else
				return -1;
			
		}
		
		
		
}

