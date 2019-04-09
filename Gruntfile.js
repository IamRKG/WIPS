module.exports = function (grunt) {

    grunt.file.setBase('WipsUIWeb/WebContent');

    var devDestination = '../../build/WipsUIWeb/WebContent';

    grunt.initConfig({

        pkg: grunt.file.readJSON('../../package.json'),

        clean: {
            dev: {
                options: {
                    force: true,
                },
                src: ['.tmp', 'dist', devDestination]
            }
        },

        useminPrepare: {
            html: 'index.html',
            options: {
                dest: 'dist'
            }
        },

        concat: {
            // dist configuration is provided by useminPrepare
            dist: {}
        },

        uglify: {
            // dist configuration is provided by useminPrepare
            dist: {}
        },
        filerev: {
            options: {
                encoding: 'utf8',
                algorithm: 'md5',
                length: 8,
                process: function (basename, name, extension) {
                    var version = new Date().getTime();
                    var filename = basename + '.' + version + '.' + extension;
                    return filename;
              }
            },

            files: {
                src: [
                    'dist/**/*.js',
                    'dist/**/*.css'
                ]
            }
        },
        usemin: {
            html: ['dist/**/*.html'],
            css: ['dist/**/*.css'],
            js: ['dist/**/*.js'],
            options: {
                assetsDirs: ['dist']
            }
        },
        copy: {
            dist: {
                expand: true,
                src: ['index.html',
                    'wipsUiApp/**/*.html',
                    'wipsUiApp/assets/**/*',
                    '!wipsUiApp/assets/css/**/*',
                    'translations/**/*',
                    'webCore/**/*.html',
                    'webCore/assets/**/*',
                    '!webCore/assets/css/**/*'
                ],
                dest: 'dist'

            },
            dev: {
                cwd: 'dist',
                expand: true,
                src: ['**/*'],
                dest: devDestination
            }
        }
    });

    var cwd = process.cwd();
    process.chdir('../../');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-filerev');
    grunt.loadNpmTasks('grunt-usemin');
    grunt.loadNpmTasks('grunt-contrib-clean');


    process.chdir(cwd);

    grunt.registerTask('build', [
        'useminPrepare',
        'concat:generated',
        'cssmin:generated',
        'uglify:generated',
        'filerev',
        'usemin'


    ]);

    grunt.registerTask('build-dev', [
        'clean:dev',
        'copy:dist',
        'build'
    ]);

    grunt.registerTask('package-dev', [
        'build-dev',
        'copy:dev'
    ]);
    grunt.registerTask('default', ['package-dev']);
};