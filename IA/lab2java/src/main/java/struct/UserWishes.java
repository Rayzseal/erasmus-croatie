package struct;

/**
 * 
 * Class UserWishes used to store all of the queries of the user. 
 * @author chloe
 *
 */
public class UserWishes {

	private String clause;
	private UserWish wish;
	
	/**
	 * Construtor of UserWishes, used to create and store a clause and the query of the user. 
	 * @param clause Clause to check. 
	 * @param wish User wish : adding, deleting, requesting. 
	 */
	public UserWishes(String clause, UserWish wish) {
		this.clause = clause;
		this.wish = wish;
	}

	/**
	 * @return the clause
	 */
	public String getClause() {
		return clause;
	}

	/**
	 * @return the wish
	 */
	public UserWish getWish() {
		return wish;
	}

	/**
	 * @param clause the clause to set
	 */
	public void setClause(String clause) {
		this.clause = clause;
	}

	/**
	 * @param wish the wish to set
	 */
	public void setWish(UserWish wish) {
		this.wish = wish;
	}

	@Override
	public String toString() {
		return "UserWishes [clause=" + clause + ", wish=" + wish + "]";
	}
	
	
	
}
