import gyges.model.Gyges;
import gyges.model.evals.BasicEval;
import gyges.model.evals.IEval;
import gyges.model.movers.IMover;
import gyges.model.movers.MTDfMover;
import gyges.model.players.AIPlayer;
import gyges.model.players.HumanPlayer;
import gyges.model.players.IPlayer;
import gyges.presenter.Presenter;
import gyges.view.IView;
import gyges.view.console.ConsoleView;
import gyges.view.gui.UiView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.cli.*;

/**
 * The Main class.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class Main
{
	/** The command line options. */
	protected static Options options;
	/** The parser. */
	protected static CommandLineParser parser;
	/** The parsed. */
	protected static CommandLine cmd = null;
	/** The depth of player 1. */
	protected static int depthJ1;
	/** The depth of player 2. */
	protected static int depthJ2;
	/** Player 1. */
	protected static IPlayer north;
	/** Player 2. */
	protected static IPlayer south;
	/** The game. */
	protected static Gyges game;
	/** The view. */
	protected static IView view;
	/** The help formatter. */
	protected static HelpFormatter help;

	/**
	 * Entry point of the program.
	 *
	 * @param args The arguments of the program.
	 */
	public static void main (String args[])
	{
		// Initialize the mover.
		IMover.init();

		// Create the options.
		options = new Options();
		options.addOption( OptionBuilder.withLongOpt("ui").withDescription("Launch with a GUI.").create("u") );
		options.addOption( OptionBuilder.withLongOpt("depth").withArgName("depth").hasArg().withDescription("Depth for both players.").create("d") );
		options.addOption( OptionBuilder.withLongOpt("depthJ1").withArgName("depth1").hasArg().withDescription("Depth for player 1.").create() );
		options.addOption( OptionBuilder.withLongOpt("ai1").withArgName("aiJ1").hasArg().withDescription("AI for player 1.").create() );
		options.addOption( OptionBuilder.withLongOpt("eval1").withArgName("evalJ1").hasArg().withDescription("Eval function for player 1.").create() );
		options.addOption( OptionBuilder.withLongOpt("pos1").withArgName("positionJ1").hasArg().withDescription("Opening position for player 1.").create() );
		options.addOption( OptionBuilder.withLongOpt("depthJ2").withArgName("depth2").hasArg().withDescription("Depth for player 2.").create() );
		options.addOption( OptionBuilder.withLongOpt("ai2").withArgName("aiJ2").hasArg().withDescription("AI for player 2.").create() );
		options.addOption( OptionBuilder.withLongOpt("eval2").withArgName("evalJ2").hasArg().withDescription("Eval function for player 2.").create() );
		options.addOption( OptionBuilder.withLongOpt("pos2").withArgName("positionJ2").hasArg().withDescription("Opening position for player 2.").create() );
		options.addOption( OptionBuilder.withLongOpt("help").withDescription("Display this help message.").create("h") );

		// Create the option parser.
		parser = new GnuParser();

		try
		{
			// Parse the command line arguments.
	        cmd = parser.parse(options, args,false);
		}
		catch (ParseException exp)
		{
			// Oops, something went wrong!
			displayHelp();
		    System.err.println("Parsing failed.\n" + exp.getMessage());
		}

		// Help display.
		if (cmd.hasOption("help")) { displayHelp(); System.exit(0); }

		// Default value for J1 & J2 depths.
		depthJ1 = depthJ2 = 2;

		// Depth option.
		if (cmd.hasOption("depth"))
		{
			// Incompatible options.
			if (cmd.hasOption("depthJ1") || cmd.hasOption("depthJ2"))
			{
				System.err.println("It's not possible to use depth with depthJ1 and/or depthJ2.");
				displayHelp();
				System.exit(1);
			}

			depthJ1 = depthJ2 = Integer.parseInt(cmd.getOptionValue("depth"));
		}

		// depthJ1 option.
		if (cmd.hasOption("depthJ1")) { depthJ1 = Integer.parseInt(cmd.getOptionValue("depthJ1")); }

		// depthJ2 option.
		if (cmd.hasOption("depthJ2")) { depthJ2 = Integer.parseInt(cmd.getOptionValue("depthJ2")); }

		// Create the movers.
		IMover mover1 = (cmd.hasOption("ai1")) ? Main.getMover("ai1", depthJ1) : new MTDfMover(depthJ1);
		IMover mover2 = (cmd.hasOption("ai2")) ? Main.getMover("ai2", depthJ2) : new MTDfMover(depthJ2);

		// Create the evals.
		IEval eval1 = (cmd.hasOption("eval1")) ? Main.getEval("eval1") : new BasicEval();
		IEval eval2 = (cmd.hasOption("eval2")) ? Main.getEval("eval2") : new BasicEval();

		// Create the players.
		north = (mover1 == null) ? new HumanPlayer("North", 0) : new AIPlayer(eval1, mover1, "North", 0);
		south = (mover2 == null) ? new HumanPlayer("South", 1) : new AIPlayer(eval2, mover2, "South", 1);

		// Create the opening positions.
		int[] posJ1 = (cmd.hasOption("pos1")) ? IPlayer.checkOpeningPosition(cmd.getOptionValue("pos1")) : null;
		int[] posJ2 = (cmd.hasOption("pos2")) ? IPlayer.checkOpeningPosition(cmd.getOptionValue("pos2")) : null;

		// Create the app presenter.
		Presenter presenter = new Presenter(north, south, posJ1, posJ2);

		// Create the view.
		if (cmd.hasOption("ui") || !north.isAI || !south.isAI )
		{
			// UI mode.
			view = new UiView(presenter);
		}
		else
		{
			// Console mode.
			view = new ConsoleView(presenter);
		}

		// Launch.
		view.init();
		presenter.setView(view);
		presenter.newGame(0);
	}

	/**
	 * Return the eval from its name.
	 *
	 * @param optionName The name of the option.
	 *
	 * @return The eval.
	 */
	protected static IEval getEval (String optionName)
	{
		try
		{
			// Get the class.
			Class<?> c = Class.forName("gyges.model.evals." + cmd.getOptionValue(optionName));

			return (IEval) c.newInstance();
		}
		catch (SecurityException | InstantiationException | IllegalAccessException  | ExceptionInInitializerError | ClassNotFoundException e)
		{
			System.out.println("Eval not found, using BasicEval instead of " + optionName);
		}

		return new BasicEval();
	}

	/**
	 * Return the mover from its name.
	 *
	 * @param optionName The name of the option.
	 * @param depth Depth of search for the mover.
	 *
	 * @return The mover.
	 */
	protected static IMover getMover (String optionName, int depth)
	{
		// No need for a mover for a human player.
		if (cmd.getOptionValue(optionName).equals("human"))
		{
			return null;
		}

		try
		{
			// Get the class.
			Class<?> c = Class.forName("gyges.model.movers." + cmd.getOptionValue(optionName));
			// Get the class constructor.
			Constructor<?> constructor = c.getConstructor(int.class);

			return (IMover) constructor.newInstance(depth);
		}
		catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			System.out.println("Mover not found, using MTDf instead of " + optionName);
		}

		return new MTDfMover(depth);
	}

	/**
	 * Display the command line argument help.
	 */
	protected static void displayHelp ()
	{
		help = new HelpFormatter();
		help.printHelp("java Main [OPTION]...\n", options);
	}
}
