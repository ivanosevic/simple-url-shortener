$(document).ready(function () {
    // We must create Clipboard...
    new ClipboardJS('a');

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
                });
            },
            error: function (data) {
                console.log(data);
                swal.fire('An error has occurred.', data.responseJSON.title);
            }
        })
    });
});