import { JAVA_JACKSON_PRESET, JAVA_COMMON_PRESET, JAVA_DESCRIPTION_PRESET, JavaFileGenerator } from '@asyncapi/modelina';
import { JAVA_JAKARTA_CONSTRAINTS_PRESET } from './JakartaConstraintsPreset';
import path from 'path';

// Where should the models be placed relative to root maven project?
// TODO: convert into the packageName used on the generator.generateToFiles call
const PACKAGE_NAME = 'io/github/ygojson/model/data/modelina'; // TODO: remove modelina
const MODEL_DIR = `src/main/java/${PACKAGE_NAME}`;

const FINAL_OUTPUT_PATH = path.resolve(__dirname, '../../', MODEL_DIR);
// Setup the generator and all it's configurations
const generator = new JavaFileGenerator({
    collectionType: 'List',
    presets: [
        JAVA_JACKSON_PRESET,
        JAVA_DESCRIPTION_PRESET,
        JAVA_JAKARTA_CONSTRAINTS_PRESET,
        JAVA_COMMON_PRESET],
});

// Load the input from file, memory, or remotely.
// Here we just use a local AsyncAPI file
import INPUT from '../../src/main/resources/schema/card.schema.json'
const input = INPUT;

// Generate all the files
generator.generateToFiles(input, FINAL_OUTPUT_PATH, {
    packageName: 'io.github.ygojson.model.data.modelina'
});
