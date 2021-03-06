var gulp = require('gulp');
var gutil = require('gulp-util');

var raml2code = require("raml2code/index.js");
var genPojos = require("raml2code/lib/generators/groovy/pojo.js");
var genJaxRs = require("raml2code/lib/generators/groovy/jaxrsInterface.js");
var genRetrofit = require("raml2code/lib/generators/java/retrofitClient.js");
var genJsClient = require("raml2code/lib/generators/javascript/jsClient");


var raml = require('gulp-raml');
var packagePojo = "gex.example.dto";
var packageJaxRs = "gex.example.jaxrs";
var packageClient = "gex.example.client";

gulp.task('raml', function() {
    gulp.src('./src/index.raml')
        .pipe(raml())
        .pipe(raml.reporter('default'));
});


gulp.task("genPojos", ['raml'], function(){
    gulp.src('./src/index.raml')
        .pipe(raml2code({generator: genPojos, extra: {package: packagePojo}}))
        .on('error', gutil.log)
        .pipe(gulp.dest('../commons/src/generated/groovy/gex/example/dto'));
});

gulp.task("genJaxRs" , ['raml'], function(){
    gulp.src('./src/index.raml')
        .pipe(raml2code({generator: genJaxRs, extra: {package: packageJaxRs, importPojos: packagePojo}}))
        .on('error', gutil.log)
        .pipe(gulp.dest('../api/src/generated/groovy/gex/example/jaxrs'));
});



gulp.task("genClient" , ['raml'], function(){
    gulp.src('./src/index.raml')
        .pipe(raml2code({generator: genRetrofit, extra: {package: packageClient, importPojos: packagePojo}}))
        .on('error', gutil.log)
        .pipe(gulp.dest('../client/src/generated/groovy/gex/example/client'));
});



gulp.task("genJsClient" , ['raml'], function(){
  gulp.src('./src/index.raml')
    .pipe(raml2code({generator: genJsClient, extra: {package: packageClient, importPojos: packagePojo}}))
    .on('error', gutil.log)
    .pipe(gulp.dest('../js-client'));
});


gulp.task('apidoc', function() {
    return gulp.src('./src/*')
        .pipe(raml2html())
        .pipe(gulp.dest('build'));
});

gulp.task('build', [
  'raml',
  'genPojos',
  'genJaxRs',
  
  'genClient',
  
  
  'genJsClient',
  
]);

