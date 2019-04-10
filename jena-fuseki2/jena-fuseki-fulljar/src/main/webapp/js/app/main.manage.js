
define( ['require', '../common-config'],
  function( require ) {
    require(
      ['underscore', 'jquery', 'backbone', 'marionette',
       'app/fui', 'app/controllers/manage-controller',
       'sprintf', 'bootstrap',
       'app/beans/fuseki-server',
       'app/beans/dataset',
       'app/beans/task',
       'app/views/dataset-management',
       'app/services/ping-service',
       'jquery.xdomainrequest'
      ],
      function( _, $, Backbone, Marionette, fui, ManageController ) {

        var options = { } ;

        // initialise the backbone application
        fui.controllers.manageController = new ManageController();
        fui.start( options );

        // additional services
        require( 'app/services/ping-service' ).start();
      });
  }
);
