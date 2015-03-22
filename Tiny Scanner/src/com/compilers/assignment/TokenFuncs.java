package com.compilers.assignment;
import com.compilers.assignment.Tokens;

public class TokenFuncs {

	//variables
	private Tokens token;
	private String string;
	private int line=0;

	//constructor
	public TokenFuncs( Tokens token, String string ){

		this.token = token;
		this.string = string;

		// get token
		if( token == Tokens.IDENTIFIER )
			this.getType();
	}

	public TokenFuncs( Tokens token, String string, int line ){

		this(token , string );
		this.line = line;
	}


	//function to return tokens
	private void getType(){

		if( this.string.equals( "if" ) )
			this.token = Tokens.IF;

		else if( this.string.equals( "then" ) )
			this.token= Tokens.THEN;

		else if( this.string.equals( "else" ) )
			this.token= Tokens.ELSE;

		else if( this.string.equals( "end" ) )
			this.token= Tokens.END;

		else if( this.string.equals( "read" ) )
			this.token = Tokens.READ;

		else if( this.string.equals( "write" ) )
			this.token = Tokens.WRITE;

		else if( this.string.equals( "repeat" ) )
			this.token = Tokens.REPEAT;

		else if( this.string.equals( "until" ) )
			this.token = Tokens.UNTIL;
	}

	public String toString(){
		return "( " + this.token + ": " + this.string + " )";
	}

	public Tokens getToken(){		
		return this.token;
	}

	public String getString(){		
		return this.string;
	}

	public int getLine(){
		return this.line;
	}
}
