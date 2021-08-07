$(document).ready(function () {
    // We must create Clipboard...
    const clipboard = new ClipboardJS('.copyable');

    // Clipboard after copied successfully
    clipboard.on('success', function (e) {
        alertify.notify('Copied! :)', 'success', 5);
    });

    clipboard.on('error', function (e) {
        alertify.notify('Error copying :(', 'error', 5);
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
