const asciidoctor = require('@asciidoctor/core')();
const asciidoctorRevealjs = require('@asciidoctor/reveal.js');
asciidoctorRevealjs.register();
const browserSync = require("browser-sync").create();

const adocFile = 'src/presentation.adoc';

function convertFile(cause) {
  asciidoctor.convertFile(adocFile, { safe: 'safe', backend: 'revealjs', to_file: 'presentation.html' });
  console.log(`Converted file: '${adocFile}' (${cause})`);
}

function watchCallback(filename) {
  if (filename.endsWith(".adoc")) {
    convertFile(`'${filename}' changed`);
    browserSync.reload();
  }
}

convertFile("startup");
browserSync.watch("src").on("change", watchCallback);
browserSync.init({
  server: true,
  startPath: "presentation.html"
});
