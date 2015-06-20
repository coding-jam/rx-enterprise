angular.module('appControllers', ['ui.router'])

    .controller('messageController', ['$scope', 'messageService', '$log', '$state', function($scope, messageService, $log, $state){

        $scope.logs = [];

        $scope.title = $state.current.data.title;

        $scope.sendMessage = function(message) {

            messageService.openSocket($state.current.data.socket)
            .then(function(socket) {

                    log("Client log: Before send message: " + message);
                    socket.send(angular.toJson({message: message}));
                    log("Client log: After send message: " + message);

                    socket.onmessage = function(event) {
                        $log.log(event);
                        $scope.$apply(function() {
                            var response = angular.fromJson(event.data);
                            log('Server log: ' + response.message + ', session id: ' + response.sessionId, response.status || "OK");

                            if (event.data.indexOf('Echo') != -1) {
                                socket.close(1000, 'Thank you');
                            }
                            if (response.status == 'ERROR') {
                                socket.close(4999, 'Server error');
                            }
                        });
                    }

            }, function(event) {
                alert("Unable to open socket :(");
            });

            function log(message, status) {
                $scope.logs.push({
                    date: new Date(),
                    message: message,
                    status: status || "OK"
                });
            }
        };

}]);