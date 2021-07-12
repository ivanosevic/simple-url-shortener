$(document).ready(function () {
    // Customs messages
    const swalWithBootstrapButtons = Swal.mixin({
        buttonsStyling: true
    });

    $('.delete-user').on('click', function (e) {
        e.preventDefault();
        const deleteUrl = $('.delete-user').attr('href');
        swalWithBootstrapButtons.fire({
            title: 'Are you sure you want to delete this user?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!',
            cancelButtonText: 'No, cancel!',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                location.href = deleteUrl;
            }
        })
    });

    $('.grant-privileges').on('click', function (e) {
        e.preventDefault();
        const grantPrivilegesUrl = $('.grant-privileges').attr('href');
        swalWithBootstrapButtons.fire({
            title: 'Are you sure you want grant privileges to this user?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes',
            cancelButtonText: 'No',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                location.href = grantPrivilegesUrl;
            }
        })
    });

    $('.take-privileges').on('click', function (e) {
        e.preventDefault();
        const takePrivilegesUrl = $('.take-privileges').attr('href');
        swalWithBootstrapButtons.fire({
            title: 'Are you sure you want take away the privileges of this user?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes',
            cancelButtonText: 'No',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                location.href = takePrivilegesUrl;
            }
        })
    });
});