/*This is a scanner class for a Tiny language compiler.
 * Made by: Karim Ahmed.
 * Department: Computer & Software Systems Engineering.
 * University: Ain Shams University, Faculty Of Engineering. 
 * 
*/

package com.compilers.assignment;
import java.io.*;
import java.util.ArrayList;


public class Scanner {
	public  int col;	
	public  int line;
	private String buffer;
	private boolean eoi=false;
	private static final int BUFFSIZE = 64;
	States currentState=States.START;
	private ArrayList<TokenFuncs> tokens;
	private String code;
	private int currentChar=0;
	private boolean save=true;


	//Constructor:

	public Scanner( InputStream streamPath ){

		this.buffer = new String();
		this.tokens = new ArrayList<TokenFuncs>();

		try {
			System.out.println("Enter the expression: ");
			char[] buff = new char[ 1024*10 ]; // 10 Kilobytes Buffer
			Reader reader = new InputStreamReader( streamPath );
			reader.read( buff );
			this.code = new String( buff ).trim();

		} 
		catch( IOException io ) {

			System.out.println( "Error reading input." );
			System.exit( 0 );
		}
	}

	//////////////////////////////////////////////////
	//Main Methods
	//Get next character
	/////////////////////////////////////////////////
	private char nextChar(){

		char ch = this.code.charAt( this.currentChar++ );

		if( this.save ){

			if( this.buffer.length() >= BUFFSIZE ){
				System.out.println( "Buffer is full, Program is exiting." );
				System.exit( 0 );
			} else
				this.buffer = this.buffer + ch;
		}
		return ch;
	}

	//Get previous character
	private void prevChar(){

		this.currentChar--;
		this.buffer = this.buffer.substring( 0, this.buffer.length()-1 );
	}

	//Verify the input doesn't exceed maximum readable code.

	private boolean hasMoreChars(){

		this.eoi = !( this.currentChar < this.code.length() );
		return !this.eoi;
	}

	//flush the buffer
	private void clearBuffer(){

		this.buffer = "";
	}

	//return token corresponding to current state:

	private Tokens stateToToken( States state ){
		Tokens token;
		switch( state ){

		case INASSIGN:
			token = Tokens.ASSIGN;
			break;

		case INID:
			token=Tokens.IDENTIFIER;
			break;

		case INNUM:
			token=Tokens.NUMBER;
			break;

		default:
			token=Tokens.NONE;
		}

		return token;
	}

	//returning token corresponding to symbol:
	private Tokens charToToken( char ch ){

		Tokens token;

		switch( ch ){

		case '=':
			token=Tokens.EQ;
			break;

		case '<':
			token=Tokens.LT;
			break;

		case '+':
			token=Tokens.PLUS;
			break;

		case '-':
			token=Tokens.MINUS;
			break;

		case '*':
			token=Tokens.MLT;
			break;

		case '/':
			token=Tokens.DIV;
			break;

		case '(':
			token=Tokens.LPAREN;
			break;

		case ')':
			token=Tokens.RPAREN;
			break;

		case ';':
			token=Tokens.SEMICOL;
			break;

		default:
			token=Tokens.NONE;
		}

		return token;
	}

	//return next token code:
	private TokenFuncs getNextToken(){

		Tokens token = Tokens.NONE;

		while( this.currentState != States.DONE && !this.eoi ){

			if( this.hasMoreChars() ){
				char ch = this.nextChar();
				switch( this.currentState ){

				case START:
					//initial state
					if( Character.isDigit( ch ) ){

						this.currentState = States.INNUM;

					} else if( Character.isLetter( ch ) ){

						this.currentState = States.INID;

					} else if( Character.isWhitespace( ch ) ){

						if( ch == '\n' )
							this.line++;
						this.clearBuffer();

					} else if( ch == ':' ){

						this.currentState = States.INASSIGN;

					} else if( ch == '{' ){

						this.currentState = States.INCOMMENT;
						this.save = false;
						this.clearBuffer();

					} else {

						this.currentState = States.DONE;
						token = this.charToToken( ch );
					}

					break;

				case INCOMMENT:

					if( ch == '}' ){

						this.currentState = States.DONE;
						this.save = true;
						token = Tokens.COMMENT;

					} else if( ch == '\n' )
						this.line++;

					break;

				case INID:

					if( !Character.isLetter( ch ) ){

						if( Character.isDigit( ch ) ){

							this.currentState = States.DONE;
							token = Tokens.NONE;
							break;
						}

						this.prevChar();
						this.currentState = States.DONE;
						token = Tokens.IDENTIFIER;
					}

					break;

				case INNUM:

					if( !Character.isDigit( ch ) ){

						if( Character.isLetter( ch ) ){

							this.currentState = States.DONE;
							token = Tokens.NONE;
							break;
						}

						this.prevChar();
						this.currentState = States.DONE;
						token = Tokens.NUMBER;
					}

					break;

				case INASSIGN:

					if( ch == '=' ){

						token = Tokens.ASSIGN;

					} else {

						this.prevChar();
						token=Tokens.NONE;
					}

					this.currentState = States.DONE;
					break;
				default:
					break;
				}	
			}
		}

		TokenFuncs token2 = null;

		if( this.currentState == States.DONE )
			token2 = new TokenFuncs( token, this.buffer, this.line );

		else if( !this.buffer.isEmpty() )
			token2 = new TokenFuncs( this.stateToToken( this.currentState ), this.buffer, this.line );

		this.currentState = States.START;
		this.clearBuffer();
		return token2;
	}


	//Lexical Analysis
	public ArrayList<TokenFuncs> analyze(){

		TokenFuncs token = this.getNextToken();

		while( token != null ){

			if( token.getToken() != Tokens.COMMENT )
				this.tokens.add( token );
			token = this.getNextToken();
		}

		return this.tokens;
	}

	
	//Print out all tokens with their description.
	public String toString(){
		String string = "All Tokens: " + this.tokens.size() + "\n\n";

		for( TokenFuncs thisToken : this.tokens )
			string += thisToken.toString() + "\n";
		System.out.println(string);
		return string;
	}

}
