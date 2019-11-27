package decaf;
import org.antlr.symtab.FunctionSymbol;
import org.antlr.symtab.GlobalScope;
import org.antlr.symtab.LocalScope;
import org.antlr.symtab.Scope;
import org.antlr.symtab.VariableSymbol;
import org.antlr.symtab.Symbol;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import java.util.ArrayList;
import org.antlr.symtab.ParameterSymbol;

/**
 * This class defines basic symbols and scopes for Decaf language
 */
public class DecafSymbolsAndScopes extends DecafParserBaseListener {
    	ParseTreeProperty<Scope> scopes = new ParseTreeProperty<Scope>();
    	ArrayList<String> varlist = new ArrayList();
	ArrayList<String> escopo = new ArrayList();
	ArrayList<String> metodo = new ArrayList();
    	GlobalScope globals;
    	Scope currentScope; // define symbols in this scope
	boolean a;
	String typeM;

    @Override
    public void enterProgram(DecafParser.ProgramContext ctx) {
        globals = new GlobalScope(null);
        pushScope(globals);	
    }
	@Override
	public void enterStatment(DecafParser.StatmentContext ctx){
		try{
			String var = ctx.location().ID().getText();
			if(! varlist.contains(var)){
				this.error(ctx.location().ID().getSymbol(), "Variável não declarada");
				System.exit(0);
			}
		}catch(Exception e){}
	}

    @Override
    public void exitProgram(DecafParser.ProgramContext ctx) {
	if(globals.resolve("main") == null){
	this.error(ctx.RCURLY().getSymbol(), "Método \'main\' não encontrado");
	System.exit(0);
	}
	popScope();
        //System.out.println(globals);
    }

   @Override
   public void enterFild_declaration(DecafParser.Fild_declarationContext ctx){
	try{
		if(Integer.parseInt(ctx.int_literal().getText()) <= 0){
			this.error(ctx.int_literal().decimal_literal().NUME().getSymbol(), "Bad Array Size");
			System.exit(0);
		}
	}catch (Exception e){}
   }

   @Override
   public void exitFild_declaration(DecafParser.Fild_declarationContext ctx){	
	for(int i = 0; i < ctx.ID().size(); i++){
		System.out.println("dsadasdada");
		String name = ctx.ID().get(i).getSymbol().getText();
		System.out.println(name);
		Symbol defineVar = currentScope.resolve(name);
		System.out.println("testeaaaaaaaaaaaaaaaaa");
		if (name.equals("false")) {
			this.error(ctx.ID().get(i).getSymbol(), "array index has wrong type");
		}
		
		if(defineVar == null){
			this.error(ctx.ID().get(i).getSymbol(), "No Such variable: " +name);
		}
	
		if(defineVar instanceof FunctionSymbol){
			this.error(ctx.ID().get(i).getSymbol(), name+"Is not a variable");
		}
   	}
}

    @Override
    public void enterMethod_declaration(DecafParser.Method_declarationContext ctx) {
        String name = ctx.ID().getText();
        this.a = false;
        this.metodo.add(name +","+ ctx.type_id().size()); // {nome,quantidade de parametros}
        //int typeTokenType = ctx.type().start.getType();
        //DecafSymbol.Type type = this.getType(typeTokenType);

	   for(int i=0;i<ctx.type_id().size();i++){
            this.escopo.add(name +","+ ctx.type_id().get(i).type().getText()); // {nome, posição do parametro, tipo do parametro}

        }

        try{
            this.typeM = ctx.type().getText();
        }catch (Exception e){}

        try{
            if(ctx.VOID().getText().equals("void")){
                this.a = true;
            }
        }catch (Exception e){}


        // push new scope by making new one that points to enclosing scope
        FunctionSymbol function = new FunctionSymbol(name);
        // function.setType(type); // Set symbol type
        
        currentScope.define(function); // Define function in current scope
        saveScope(ctx, function);
        pushScope(function);
    }

    @Override
    public void exitMethod_declaration(DecafParser.Method_declarationContext ctx) {
	    try{
            //metodo void
            if(a == true){
                for(int i=0;i<ctx.block().statment().size();i++){
                    if(ctx.block().statment().get(i).RETURN().getText().equals("return")){
                        this.error(ctx.block().statment().get(i).RETURN().getSymbol(),"should not return value");
                        System.exit(0);
                    }
                }
            }else{ //metodo não é void
//                System.out.println("Tipo do retorno -> "+this.typeMethod);
                for(int i=0;i<ctx.block().statment().size();i++){
                    //verifica o retorno
                    if(ctx.block().statment().get(i).RETURN().getText().equals("return")){
                        for(int j=0;j<ctx.block().statment().get(i).expr().size();j++) {
                            //entra no literal (boolean ou int)
                            if(ctx.block().statment().get(i).expr().get(j).literal() != null){
                                String literal = ctx.block().statment().get(i).expr().get(j).literal().getText();
                                //caso o tipo seja 'boolean' e receba um valor diferente
                                if(this.typeM.equals("boolean") && !(literal.equals("false") || literal.equals("true"))){
                                    System.out.println("Esperando um boolean");
                                    this.error(ctx.ID().getSymbol(),"return value has wrong type");
                                    System.exit(0);
                                }
                                //caso o tipo seja 'int' e receba um valor diferente
                                 if(this.typeM.equals("int") && !(this.numbers(literal))){
                                 System.out.println("Esperando um int");
                                 this.error(ctx.ID().getSymbol(),"return value has wrong type");
                                 System.exit(0);
                                }
                            }
                            // entra no location (variável)
                            if(ctx.block().statment().get(i).expr().get(i).location() != null){

                            }
                        }
                    }
                }
            }
        }catch (Exception e){}
        popScope();
    }
	@Override
		public void enterMethod_call(DecafParser.Method_callContext ctx){
	}

    @Override public void exitMethod_call(DecafParser.Method_callContext ctx){
        try{
            for(int i = 0 ; i < ctx.expr().size(); i++){
            if(!this.metodo.contains(ctx.method_name().ID().getText()+","+ ctx.expr().size())){ 
                this.error(ctx.expr(0).literal().int_literal().decimal_literal().NUME().getSymbol(),"argument mismatch");
                System.exit(0);
            }    
        }
    	} catch(Exception e){}
	
	try{
        	for(int i=0;i<ctx.expr().size();i++) {
		        if(ctx.expr().get(i).literal() != null){ }
		        
			if(ctx.expr().get(i).literal().getText().equals("false") || ctx.expr().get(i).literal().getText().equals("true")){
		                if(!this.escopo.contains(ctx.method_name().ID().getText()+","+ i)){
		                    this.error(ctx.expr(0).literal().bool_literal().BOOLEANLITERAL().getSymbol(),"types don't match signature");
		                    System.exit(0);
		                }
		        }else{
		               if(!this.escopo.contains(ctx.method_name().ID().getText()+","+ i)) {
		                    this.error(ctx.expr(1).literal().int_literal().decimal_literal().NUME().getSymbol(),"types don't match signature");
		                    System.exit(0);
		               }
		        }   
                }
         } catch(Exception e){}
      }

    @Override
	public void enterVar_declaration(DecafParser.Var_declarationContext ctx){
		String variavel_local = "";
		for (int i = 0; i < ctx.ID().size(); i++){
		variavel_local = variavel_local + ctx.ID().get(i).getText();
		defineVar(ctx.type(), ctx.ID().get(i).getSymbol());
		this.varlist.add(ctx.ID().get(i).getText());
	}
    }
    @Override
	public void exitVar_declaration(DecafParser.Var_declarationContext ctx){
	for(int i = 0 ; i< ctx.ID().size(); i++){
        String name = ctx.ID().get(i).getSymbol().getText();
        Symbol var = currentScope.resolve(name);
        if ( var==null ) {
            this.error(ctx.ID().get(i).getSymbol(), "no such variable: "+name);
        }
        if ( var instanceof FunctionSymbol ) {
            this.error(ctx.ID().get(i).getSymbol(), name+" is not a variable");
        }
	}
    }

    @Override
    public void enterBlock(DecafParser.BlockContext ctx) {
        /*LocalScope l = new LocalScope(currentScope);
        saveScope(ctx, currentScope);
        pushScope(l);*/
    }

    @Override
    public void exitBlock(DecafParser.BlockContext ctx) {
        //popScope();
    }

    @Override
    public void enterType_id(DecafParser.Type_idContext ctx) {	
	//for(int i = 0 ; i< ctx.ID().size(); i++){
        defineVar(ctx.type(), ctx.ID().getSymbol());
	this.varlist.add(ctx.ID().getText());
    	//}
    }

    @Override
    public void exitType_id(DecafParser.Type_idContext ctx) {
	//for(int i = 0 ; i< ctx.ID().size(); i++){
        String name = ctx.ID().getSymbol().getText();
        Symbol var = currentScope.resolve(name);
        if ( var==null ) {
            this.error(ctx.ID().getSymbol(), "no such variable: "+name);
        }
        if ( var instanceof FunctionSymbol ) {
            this.error(ctx.ID().getSymbol(), name+" is not a variable");
        }
	//}
    }

    void defineVar(DecafParser.TypeContext typeCtx, Token nameToken) {
        //int typeTokenType = typeCtx.start.getType();
        VariableSymbol var = new VariableSymbol(nameToken.getText());

        // DecafSymbol.Type type = this.getType(typeTokenType);
        // var.setType(type);

        currentScope.define(var); // Define symbol in current scope
    }

    /**
     * Método que atuliza o escopo para o atual e imprime o valor
     *
     * @param s
     */
    private void pushScope(Scope s) {
        currentScope = s;
        System.out.println("entering: "+currentScope.getName()+":"+s);
    }

    /**
     * Método que cria um novo escopo no contexto fornecido
     *
     * @param ctx
     * @param s
     */
    void saveScope(ParserRuleContext ctx, Scope s) {
        scopes.put(ctx, s);
    }

    /**
     * Muda para o contexto superior e atualia o escopo
     */
    private void popScope() {
        System.out.println("leaving: "+currentScope.getName()+":"+currentScope);
        currentScope = currentScope.getEnclosingScope();
    }

    public static void error(Token t, String msg) {
        System.err.printf("line %d:%d %s\n", t.getLine(), t.getCharPositionInLine(),
                msg);
    }

    /**
     * Valida tipos encontrados na linguagem para tipos reais
     *
     * @param tokenType
     * @return
     */
    public static DecafSymbol.Type getType(int tokenType) {
        switch ( tokenType ) {
            case DecafParser.VOID :  return DecafSymbol.Type.tVOID;
            case DecafParser.INT:   return DecafSymbol.Type.tINT;
	    case DecafParser.BOOLEAN:   return DecafSymbol.Type.tBOOLEAN;
	  
        }
        return DecafSymbol.Type.tINVALID;
    }

    public boolean numbers (String texto){
	for(int i = 0; i < texto.length(); i++){
		if(!Character.isDigit(texto.charAt(i))){
			return false;
		}
	}
	return true;
    }
}
