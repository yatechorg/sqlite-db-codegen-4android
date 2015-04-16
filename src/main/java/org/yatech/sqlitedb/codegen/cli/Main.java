package org.yatech.sqlitedb.codegen.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	
	private static final Options OPTIONS = defineOptions();
	
	public static void main(String[] args) {
		CommandLine commandLine = parseCliArguments(args);
		run(commandLine);
	}

	private static Options defineOptions() {
		Options options = new Options();
		options.addOption(buildOption('p',"pkg","Name of the package for the generated classes",true,"NAME"));
		options.addOption(buildOption('s',"src","Path to the source directory",true,"PATH"));
		return options;
	}

	@SuppressWarnings("static-access")
	private static Option buildOption(char opt, String longOpt, String desc, boolean required, String argName) {
		return OptionBuilder
				.withLongOpt(longOpt)
				.hasArg()
				.withArgName(argName)
				.withDescription(desc)
				.isRequired(required)
				.withType(String.class)
				.withValueSeparator(' ')
				.create(opt);
	}

	private static CommandLine parseCliArguments(String[] args) {
		CommandLine commandLine = null;
		try {
			CommandLineParser cliParser = new GnuParser();
			commandLine = cliParser.parse(OPTIONS, args);
		} catch (ParseException e) {
			printUsage(OPTIONS);
			System.exit(1);
		}
		return commandLine;
	}

	private static void printUsage(Options options2) {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("codegen", OPTIONS, true);
	}

	private static void run(CommandLine commandLine) {
		// TODO Auto-generated method stub
	}

}
