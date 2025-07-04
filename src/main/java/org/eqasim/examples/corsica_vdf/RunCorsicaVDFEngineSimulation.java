package org.eqasim.examples.corsica_vdf;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.eqasim.core.simulation.vdf.VDFConfigGroup;
import org.eqasim.core.simulation.vdf.engine.VDFEngineConfigGroup;
import org.eqasim.ile_de_france.IDFConfigurator;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.CommandLine;
import org.matsim.core.config.CommandLine.ConfigurationException;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;

import com.google.common.io.Resources;

/**
 * This is an example run script that runs the Corsica test scenario with a
 * volume-delay function to simulate travel times.
 */
public class RunCorsicaVDFEngineSimulation {
	static public void main(String[] args) throws ConfigurationException {
		CommandLine cmd = new CommandLine.Builder(args) //
				.allowOptions("config-path") //
				.allowPrefixes("mode-parameter", "cost-parameter") //
				.build();

		IDFConfigurator configurator = new IDFConfigurator(cmd);

		String configPath = cmd.getOptionStrict("config-path");
		Config config = ConfigUtils.loadConfig(configPath);
		configurator.updateConfig(config);

		// config.controller().setLastIteration(2);

		// VDF: Add config group
		// config.addModule(new VDFConfigGroup());

		// VDF: Disable queue logic
		config.qsim().setFlowCapFactor(1e9);
		config.qsim().setStorageCapFactor(1e9);

		// VDF: Set capacity factor that can be used for calibration (in eqasim it does not correlate with
		// sample size as this is controlled by the samplingRate in eqasim config group

		VDFConfigGroup.getOrCreate(config).setCapacityFactor(1);

		// VDF: Optional
		VDFConfigGroup.getOrCreate(config).setWriteInterval(1);
		VDFConfigGroup.getOrCreate(config).setWriteFlowInterval(1);

		// VDF Engine: Add config group
		// config.addModule(new VDFEngineConfigGroup());

		// VDF Engine: Decide whether to genertae link events or not
		VDFEngineConfigGroup.getOrCreate(config).setGenerateNetworkEvents(true);

		// VDF Engine: Remove car from main modes
		Set<String> mainModes = new HashSet<>(config.qsim().getMainModes());
		mainModes.remove("car");
		config.qsim().setMainModes(mainModes);

		Scenario scenario = ScenarioUtils.createScenario(config);
		configurator.configureScenario(scenario);
		ScenarioUtils.loadScenario(scenario);

		Controler controller = new Controler(scenario);
		configurator.configureController(controller);

		controller.run();
	}
}