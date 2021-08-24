/**
 * Until document is ready...
 */

document.addEventListener("DOMContentLoaded", function (event) {
  microlink('.link-preview', {
    media: ['image','logo']
  });

  const clipboard = new ClipboardJS('.copyable');

  clipboard.on('success', function () {
    alertify.notify('Copied! :)', 'success', 5);
  });

  clipboard.on('error', function () {
    alertify.notify('Error copying :(', 'error', 5);
  });

  document.addEventListener('click', function (clickEvent) {
    qrCodeBtn(clickEvent);
    microLinkBtn(clickEvent);
  });

});

const qrCodeBtn = function (evt) {
  if (evt.target.matches('.qr-code')) {
    evt.preventDefault();
    let image = evt.target.getAttribute('qr-image');
    swal.fire({
      imageUrl: image,
      imageWidth: 400,
      imageHeight: 400,
      showConfirmButton: false,
      showCloseButton: true
    });
  }
};

const microLinkBtn = function (evt) {
  console.log("I'm here!");
  if(evt.target.matches('.link-preview')) {
    evt.preventDefault();
    console.log("I'm inside  the link preview event.");
    let shortUrl = evt.target.getAttribute('data-short-url');
  }
};
