
function GeoIpController($scope, $http) {
    $scope.country = {name : "n.a.", code: "n.a."};

    $scope.getCountry = function() {
        $http.get('/rest/resolve').success(function (data) {
            $scope.country = data;
        });
    }
}
