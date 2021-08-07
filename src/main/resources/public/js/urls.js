$(document).ready(function () {
    // Customs messages
    const swalWithBootstrapButtons = Swal.mixin({
        buttonsStyling: true
    });

    // Event delegator for asking the API for a QR CODE.
    $(document).on('click', '.qr-code', function (e) {
        e.preventDefault();
        let code = $(this).attr('data-code-id');
        console.log(code);
        $.ajax({
            url: '/qr/' + code,
            type: 'get',
            dataType: 'json',
            success: function (data) {
                console.log(data);
                swal.fire({
                    imageUrl: data.imageBase64,
                    imageWidth: 400,
                    imageHeight: 400,
                    imageAlt: data.redirectUrl,
                    showConfirmButton: false,
                    showCloseButton: true
                });
            },
            error: function (data) {
                console.log(data);
                swal.fire('An error has occurred.', data.responseJSON.title);
            }
        })
    });

    $('.delete-url').on('click', function (e) {
        e.preventDefault();
        const deleteUrl = $('.delete-url').attr('href');
        swalWithBootstrapButtons.fire({
            title: 'Are you sure you want to delete this URL?',
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

    //Microlink needs original urls in order to work. So in this case, after loading the link previews with the original URLs
    //we change back the urls of the links to the shorten ones.
    $(document).on('click','.link-preview',function(e){
        let short_url = $(this).attr('data-short-url');
        $(this).attr("href",short_url)
    });

});

//Microlink implementation
document.addEventListener('DOMContentLoaded', function (event) {
    // Replace all `link-preview` tags for microlink cards
    microlink('.link-preview', {
        media: ['image','logo']
    })
})
