package io.github.ygojson.cli;

import io.quarkus.arc.Unremovable;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import picocli.CommandLine;

import io.github.ygojson.application.ApplicationInfo;

/**
 * Top command for YGOJSON-tools.
 * <br>
 * Note that the application name is provided by the modelTransformer
 */
@TopCommand
@CommandLine.Command(
	description = "Tools to run the data-collection and bundling for YGOJSON",
	abbreviateSynopsis = true,
	versionProvider = YgoJsonTopCommand.VersionProvider.class,
	modelTransformer = YgoJsonTopCommand.ModelTransformer.class,
	mixinStandardHelpOptions = true,
	subcommands = { CommandLine.HelpCommand.class }
)
public class YgoJsonTopCommand {

	@Unremovable
	@Singleton
	static final class ModelTransformer implements CommandLine.IModelTransformer {

		private final ApplicationInfo applicationInfo;

		@Inject
		public ModelTransformer(final ApplicationInfo info) {
			this.applicationInfo = info;
		}

		@Override
		public CommandLine.Model.CommandSpec transform(
			CommandLine.Model.CommandSpec commandSpec
		) {
			return commandSpec.name(applicationInfo.name());
		}
	}

	@Unremovable
	@Singleton
	static final class VersionProvider implements CommandLine.IVersionProvider {

		private final ApplicationInfo applicationInfo;

		@Inject
		public VersionProvider(final ApplicationInfo info) {
			this.applicationInfo = info;
		}

		@Override
		public String[] getVersion() {
			return new String[] {
				String.format(
					"%s v.%s",
					applicationInfo.name(),
					applicationInfo.version()
				),
			};
		}
	}
}
