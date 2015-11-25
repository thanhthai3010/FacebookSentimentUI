/**
 * 
 */
package spring.fb.demo.services;

/**
 * @author QuanTA5
 *
 */
//@Component
public class VietSentiService {
		
	
	public VietSentiService(){
		
		//ExtractOpinion eoDemo = new ExtractOpinion();
		//System.out.println("" + ExtractOpinion.runAnalyze("Hôm nay anh không được vui lắm em à, anh nhớ em nhiều lắm!"));
	}
	
	/*public static void main(String[] args) {
		SparkUtil.createJavaSparkContext();
		VietTokenizer tokenizer = new VietTokenizer();
		Checker check = new Checker();

		VietSentiData.init();
		String demo = "Hôm nay anh không vui và cũng không hạnh phúc tí nào em à!";
		// check spell

		//String[] result = tokenizer.tokenize(demo);
		String[] rsCheckSpell = check.correct(tokenizer.tokenize(demo));
		// System.err.println(rsCheckSpell.toString());
		double rs = VietSentiData.scoreTokens(rsCheckSpell);
		System.out
				.println("--------------------------------------------------------------------------------");
		System.out.println("input text: " + rsCheckSpell[0]);
		System.out.println("value of score: " + rs);
		System.out
				.println("--------------------------------------------------------------------------------");
	}*/
}
